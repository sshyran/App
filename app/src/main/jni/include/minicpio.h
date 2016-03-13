#ifndef _MINICPIO_H_
#define _MINICPIO_H_

typedef struct mini_cpio_hdr mini_cpio_hdr;

#define CPIO_MAGIC "070701"

struct mini_cpio_hdr
{
    unsigned char magic[6];
    unsigned char ino[8];
    unsigned char mode[8];
    unsigned char uid[8];
    unsigned char gid[8];
    unsigned char nlink[8];
    unsigned char mtime[8];
    unsigned char filesize[8];
    unsigned char dev_maj[8];
    unsigned char dev_min[8];
    unsigned char rdev_maj[8];
    unsigned char rdev_min[8];
    unsigned char namesize[8];
    unsigned char chksum[8];
};


#endif
