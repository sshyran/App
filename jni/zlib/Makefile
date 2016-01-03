CC := gcc
export SRC_FILES := test/minigzip.c adler32.c \
             compress.c crc32.c deflate.c \
             gzclose.c gzlib.c gzread.c \
             gzwrite.c infback.c inflate.c \
             inftrees.c inffast.c slhash.c  \
             trees.c uncompr.c zutil.c

CFLAGS := -static -O2 -pie -fPIC
LDFLAGS := $(CFLAGS) -lz
TARGET := ../libminigzip.a
SRC_FILES := $(shell ls *.c) 
AR := ar r
%.o:%.c
	@echo "      Compiling    $^"
	@$(CC) $(CFLAGS) -c $< -o $@

all:$(TARGET)

$(TARGET):${SRC_FILES:.c=.o}
	@echo "      ARLD   $@"
	@$(AR) $@ $^

.PHONY:clean
clean:
	-rm -f *.o $(TARGET)
