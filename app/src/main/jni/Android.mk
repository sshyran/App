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
					minigzip.c
					
             		

LOCAL_MODULE:= bootimg
LOCAL_C_INCLUDE_PATH := $(LOCAL_PATH) $(LOCAL_PATH)/include
LOCAL_LDLIBS += -L$(SYSROOT)/usr/lib -llog
LOCAL_LDLIBS += -L$(SYSROOT)/usr/lib -lc
LOCAL_LDLIBS += -L$(SYSROOT)/usr/lib -lz
LOCAL_CFLAGS += -fPIE -pie
include $(BUILD_SHARED_LIBRARY)

