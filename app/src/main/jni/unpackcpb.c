/*
 * =====================================================================================
 *
 *       Filename:  cpbtool.c
 *
 *    Description:  
 *
 *        Version:  1.0
 *        Created:  2013年05月07日 18时55分53秒
 *       Revision:  none
 *       Compiler:  gcc
 *
 *         Author:  linkscue (scue), 
 *   Organization:  ATX风雅组
 *
 * =====================================================================================
 */

#include    <stdio.h>
#include    <stdlib.h>
#include    "include/bootimg.h"
#include <unistd.h>

#define u8 unsigned char
#define u32 unsigned int
#define u16 unsigned short

typedef struct {
    u8 cp_magic[4];                             /* coolpad file magic */
    u8 cp_version[32];                          /* coolpad head version */
    u8 model[32];                               /* coolpad phone model */
    u8 flag_p2[16];                             /* alway is P2 string */
    u8 version[64];                             /* phone version or rom name */
    u8 file_form[256];                          /* where the rom come from */
    u8 information[12];                         /* some information, but unkown */
    u32 image_offset;                           /* entrance offset of image */
    u32 cpb_filesize;                           /* the size of whole cpb file */
    u8 reverse[128];                            /* never use, remain for future */
    u32 checksum;                               /* here maybe is a checksum */
} cpb_head;

typedef struct {                                /* 76 bytes */
    u8 filename[64];                            /* image filename */
    u32 image_offset;                           /* image offset */
    u32 image_size;                             /* image filesize */
    u32 checksum;                               /* here maybe is a checksum */
} image_t;

//分解文件函数；
int splitFile(char *file){

    FILE *fd = NULL;
    FILE *ft = NULL;
    int i=0,imagecount=0;
    cpb_head header;
    image_t images[10];
    if ( (fd=fopen(file,"rb")) == NULL ) {      /* 打开文件进行操作 */
        return 1;
    }

    fread(&header, sizeof(header), 1, fd);
    for ( i=0; ( ftell(fd) < (header.image_offset) ); i++ ){
        fread(&images[i], sizeof(image_t), 1, fd);
        imagecount++;
    }
    //开始解压数据；
    int size=0,n=0,count=0,offset=0;
    unsigned char imagename[32]="";
    unsigned char buffer[4]="";              /* 创建缓冲区 */
    for( i=0; i < imagecount; i++ ){
        strncpy(imagename, images[i].filename, sizeof(imagename));
        offset=images[i].image_offset;
        size=images[i].image_size;
        if ( size != 0 ) {
            if ( ( ft=fopen(imagename,"wb") ) == NULL ){
                return 1;
            }
            fseek( fd, offset, SEEK_SET);                /* 跳转至数据段 */
            /*printf("提取:%-15s\n偏移:0x%08x大小:%d\n",imagename, offset, size);*/
            n=0;count=0;
            while ( count < size )  {
                n  = fread(buffer,1, sizeof(buffer), fd);
                fwrite(buffer, n, 1, ft);
                count+=n;
            }
        }
    }
    fclose(fd);
    return 0;
}

/* 
 * ===  FUNCTION  ======================================================================
 *         Name:  main
 *  Description:  仅实现了分解文件的功能
 * =====================================================================================
 */
int unpackcpb_main(int argc, char *argv[])
{
			if(argv[1] && argv[2]){
			if(exist(argv[2])){
    	mkdir(argv[2], 0777);
    }
				chdir(argv[2]);
    	return splitFile(argv[1]);
    }
    return 1;
}
