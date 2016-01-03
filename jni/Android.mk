LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

LOCAL_SRC_FILES:= app.c \
					unpackbootimg.c \
					repackbootimg.c \
					rsa.c \
					sha.c \
					unpackapp.c \
					unpackcpb.c \
					simg2img.c \
					sparse_crc32.c \
					bootimg.c \
					zlib/test/minigzip.c \
					zlib/adler32.c \
					zlib/compress.c \
					zlib/crc32.c \
					zlib/deflate.c \
					zlib/gzclose.c \
					zlib/gzlib.c \
					zlib/gzread.c \
					zlib/gzwrite.c \
					zlib/infback.c \
					zlib/inflate.c \
					zlib/inftrees.c \
					zlib/inffast.c \
					zlib/slhash.c  \
					zlib/trees.c \
					zlib/uncompr.c \
					zlib/zutil.c
      
             		

LOCAL_MODULE:= bootimg
LOCAL_CFLAGS := -I$(LOCAL_PATH) -I$(LOCAL_PATH)/include -pie
LOCAL_LDLIBS := -llog
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)

LOCAL_SRC_FILES:= uncpio.c
					                   
LOCAL_MODULE := uncpio
LOCAL_CFLAGS := -I$(LOCAL_PATH) -I$(LOCAL_PATH)/include -pie
include $(BUILD_EXECUTABLE)

include $(CLEAR_VARS)

LOCAL_SRC_FILES:= mkcpio.c
					                   
LOCAL_MODULE:= mkcpio
LOCAL_CFLAGS := -I$(LOCAL_PATH) -I$(LOCAL_PATH)/include -pie
include $(BUILD_EXECUTABLE)