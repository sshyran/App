#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <errno.h>
#include <limits.h>
#include <libgen.h>
#include "include/sha.h"
#include "include/bootimg.h"
#include "include/bootimg_tools.h"

typedef unsigned char byte;

int read_padding(FILE* f, unsigned itemsize, int pagesize)
{
    byte* buf = (byte*)malloc(sizeof(byte) * pagesize);
    unsigned pagemask = pagesize - 1;
    unsigned count;

    if((itemsize & pagemask) == 0) {
        free(buf);
        return 0;
    }

    count = pagesize - (itemsize & pagemask);

    fread(buf, count, 1, f);
    free(buf);
    return count;
}

void write_string_to_file(char* file, char* string)
{
    FILE* f = fopen(file, "w");
    fwrite(string, strlen(string), 1, f);
    fwrite("\n", 1, 1, f);
    fclose(f);
}

int __inline unpack_usage() {
    printf("语法:--unpackbootimg [附加参数]\n");
    printf("              --input      <指定boot.img>\n");
    printf("              --output     <指定输出目录>\n");
    printf("              --pagesize   <2048或4096>\n");
    return 0;
}

int unpackbootimg_main(int argc, char** argv)
{
    char tmp[PATH_MAX];
    char* directory = "./";
    char* filename = NULL;
    int pagesize = 0;

    argc--;
    argv++;
    while(argc > 0){
        char *arg = argv[0];
        char *val = argv[1];
        argc -= 2;
        argv += 2;
        if(!strcmp(arg, "--input") || !strcmp(arg, "-i")) {
            filename = val;
        } else if(!strcmp(arg, "--output") || !strcmp(arg, "-o")) {
            directory = val;
            mkdir(directory);
        } else if(!strcmp(arg, "--pagesize") || !strcmp(arg, "-p")) {
            pagesize = strtoul(val, 0, 16);
        } else {
            return unpack_usage();
        }
    }

    if (filename == NULL) {
        return unpack_usage();
    }

    int total_read = 0;
    FILE* f = fopen(filename, "rb");
    boot_img_hdr header;

    fread(&header, sizeof(header), 1, f);

    if (pagesize == 0) {
        pagesize = header.page_size;
    }

    sprintf(tmp, "%s/%s", directory, CMDLINE_FILE);
    write_string_to_file(tmp, header.cmdline);

    sprintf(tmp, "%s/%s", directory, BASE_FILE);
    char basetmp[200];
    sprintf(basetmp, "%08x", header.kernel_addr - 0x00008000);
    write_string_to_file(tmp, basetmp);

    sprintf(tmp, "%s/%s", directory, PAGESIZE_FILE);
    char pagesizetmp[200];
    sprintf(pagesizetmp, "%d", header.page_size);
    write_string_to_file(tmp, pagesizetmp);

    total_read += sizeof(header);
    total_read += read_padding(f, sizeof(header), pagesize);

    sprintf(tmp, "%s/%s", directory, KERNEL_FILE);
    FILE *k = fopen(tmp, "wb");
    byte* kernel = (byte*)malloc(header.kernel_size);
    fread(kernel, header.kernel_size, 1, f);
    total_read += header.kernel_size;
    fwrite(kernel, header.kernel_size, 1, k);
    fclose(k);
    total_read += read_padding(f, header.kernel_size, pagesize);

    sprintf(tmp, "%s/%s", directory, RAMDISK_FILE);
    FILE *r = fopen(tmp, "wb");
    byte* ramdisk = (byte*)malloc(header.ramdisk_size);
    fread(ramdisk, header.ramdisk_size, 1, f);
    total_read += header.ramdisk_size;
    fwrite(ramdisk, header.ramdisk_size, 1, r);
    fclose(r);

    total_read += read_padding(f, header.ramdisk_size, pagesize);

    sprintf(tmp, "%s/%s", directory, DT_IMG_FILE);
    FILE *d = fopen(tmp, "wb");
    byte* dt = (byte*)malloc(header.dt_size);
    fread(dt, header.dt_size, 1, f);
    total_read += header.dt_size;
    fwrite(dt, header.dt_size, 1, r);

    fclose(d);

    fclose(f);
    return 0;
}
