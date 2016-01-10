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
LOCAL_CFLAGS := -I$(LOCAL_PATH) -I$(LOCAL_PATH)/include -pie
LOCAL_LDLIBS := -llog -lc -lz
LOCAL_CFLAGS += -fPIE -pie
include $(BUILD_SHARED_LIBRARY)

