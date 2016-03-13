/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
#define _LARGEFILE64_SOURCE
#define _FILE_OFFSET_BITS 64

#include <sys/types.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <sys/mman.h>
#include <unistd.h>
#include <fcntl.h>
#include <stdio.h>

#include "include/ext4_utils.h"
#include "include/output_file.h"
#include "include/sparse_format.h"
#include "include/sparse_crc32.h"

#if defined(__APPLE__) && defined(__MACH__)
#define lseek64 lseek
#define off64_t off_t
#endif

#define COPY_BUF_SIZE (1024*1024)
u8 *copybuf;

/* This will be malloc'ed with the size of blk_sz from the sparse file header */
u8* zerobuf;

#define SPARSE_HEADER_MAJOR_VER 1
#define SPARSE_HEADER_LEN       (sizeof(sparse_header_t))
#define CHUNK_HEADER_LEN (sizeof(chunk_header_t))

void simg2img_usage()
{
  fprintf(stderr, "语法:simg2img <sparse_image_file> <raw_image_file>\n");
}

int process_raw_chunk(FILE *in, FILE *out, u32 blocks, u32 blk_sz, u32 *crc32)
{
	u64 len = (u64)blocks * blk_sz;
	int chunk;

	while (len) {
		chunk = (len > COPY_BUF_SIZE) ? COPY_BUF_SIZE : len;
		if (fread(copybuf, chunk, 1, in) != 1) {
			fprintf(stderr, "fread returned an error copying a raw chunk\n");
			return 1;
		}
		*crc32 = sparse_crc32(*crc32, copybuf, chunk);
		if (fwrite(copybuf, chunk, 1, out) != 1) {
			fprintf(stderr, "fwrite returned an error copying a raw chunk\n");
			return 1;
		}
		len -= chunk;
	}

	return blocks;
}


int process_skip_chunk(FILE *out, u32 blocks, u32 blk_sz, u32 *crc32)
{
	/* len needs to be 64 bits, as the sparse file specifies the skip amount
	 * as a 32 bit value of blocks.
	 */
	u64 len = (u64)blocks * blk_sz;
	u64 len_save;
	u32 skip_chunk;

	/* Fseek takes the offset as a long, which may be 32 bits on some systems.
	 * So, lets do a sequence of fseeks() with SEEK_CUR to get the file pointer
	 * where we want it.
	 */
	len_save = len;
	while (len) {
		skip_chunk = (len > 0x80000000) ? 0x80000000 : len;
		fseek(out, skip_chunk, SEEK_CUR);
		len -= skip_chunk;
	}
	/* And compute the CRC of the skipped region a chunk at a time */
	len = len_save;
	while (len) {
		skip_chunk = (skip_chunk > blk_sz) ? blk_sz : skip_chunk;
		*crc32 = sparse_crc32(*crc32, zerobuf, skip_chunk);
		len -= skip_chunk;
	}

	return blocks;
}

int simg2img_main(int argc, char *argv[])
{
	FILE *in, *out;
	unsigned int i;
	sparse_header_t sparse_header;
	chunk_header_t chunk_header;
	u32 crc32 = 0;
	u32 total_blocks = 0;

	if (argc != 3) {
		simg2img_usage();
		return 1;
	}

	if ( (copybuf = malloc(COPY_BUF_SIZE)) == 0) {
		fprintf(stderr, "Cannot malloc copy buf\n");
		return 1;
	}

	if ((in = fopen(argv[1], "rb")) == 0) {
		fprintf(stderr, "Cannot open input file %s\n", argv[1]);
		return 1;
	}

	if ((out = fopen(argv[2], "wb")) == 0) {
		fprintf(stderr, "Cannot open output file %s\n", argv[2]);
		return 1;
	}

	if (fread(&sparse_header, sizeof(sparse_header), 1, in) != 1) {
		fprintf(stderr, "Error reading sparse file header\n");
		return 1;
	}

	if (sparse_header.magic != SPARSE_HEADER_MAGIC) {
		fprintf(stderr, "Bad magic\n");
		return 1;
	}

	if (sparse_header.major_version != SPARSE_HEADER_MAJOR_VER) {
		fprintf(stderr, "Unknown major version number\n");
		return 1;
	}

	if (sparse_header.file_hdr_sz > SPARSE_HEADER_LEN) {
		/* Skip the resimg2imging bytes in a header that is longer than
		 * we expected.
		 */
		fseek(in, sparse_header.file_hdr_sz - SPARSE_HEADER_LEN, SEEK_CUR);
	}

	if ( (zerobuf = malloc(sparse_header.blk_sz)) == 0) {
		fprintf(stderr, "Cannot malloc zero buf\n");
		return 1;
	}

	for (i=0; i<sparse_header.total_chunks; i++) {
		if (fread(&chunk_header, sizeof(chunk_header), 1, in) != 1) {
			fprintf(stderr, "Error reading chunk header\n");
			return 1;
		}
 
		if (sparse_header.chunk_hdr_sz > CHUNK_HEADER_LEN) {
			/* Skip the resimg2imging bytes in a header that is longer than
			 * we expected.
			 */
			fseek(in, sparse_header.chunk_hdr_sz - CHUNK_HEADER_LEN, SEEK_CUR);
		}

		switch (chunk_header.chunk_type) {
		    case CHUNK_TYPE_RAW:
			if (chunk_header.total_sz != (sparse_header.chunk_hdr_sz +
				 (chunk_header.chunk_sz * sparse_header.blk_sz)) ) {
				fprintf(stderr, "Bogus chunk size for chunk %d, type Raw\n", i);
				return 1;
			}
			total_blocks += process_raw_chunk(in, out,
					 chunk_header.chunk_sz, sparse_header.blk_sz, &crc32);
			break;
		    case CHUNK_TYPE_DONT_CARE:
			if (chunk_header.total_sz != sparse_header.chunk_hdr_sz) {
				fprintf(stderr, "Bogus chunk size for chunk %d, type Dont Care\n", i);
				return 1;
			}
			total_blocks += process_skip_chunk(out,
					 chunk_header.chunk_sz, sparse_header.blk_sz, &crc32);
			break;
		    default:
			fprintf(stderr, "Unknown chunk type 0x%4.4x\n", chunk_header.chunk_type);
			return 1;
		}

	}

	/* If the last chunk was a skip, then the code just did a seek, but
	 * no write, and the file won't actually be the correct size.  This
	 * will make the file the correct size.  Make sure the offset is
	 * computed in 64 bits, and the function called can handle 64 bits.
	 */
	if (ftruncate(fileno(out), (u64)total_blocks * sparse_header.blk_sz)) {
		fprintf(stderr, "Error calling ftruncate() to set the image size\n");
		return 1;
	}

	fclose(in);
	fclose(out);

	if (sparse_header.total_blks != total_blocks) {
		fprintf(stderr, "Wrote %d blocks, expected to write %d blocks\n",
			 total_blocks, sparse_header.total_blks);
		return 1;
	}

	if (sparse_header.image_checksum != crc32) {
		fprintf(stderr, "computed crc32 of 0x%8.8x, expected 0x%8.8x\n",
			 crc32, sparse_header.image_checksum);
		return 0;
	}

	return 0;
}

