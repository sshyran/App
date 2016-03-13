LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_SRC_FILES:= sdat2img.c
					                   
LOCAL_MODULE := sdat2img
LOCAL_CFLAGS := -I$(LOCAL_PATH)
LOCAL_CFLAGS += -pie -fPIE
LOCAL_LDFLAGS += -pie -fPIE
include $(BUILD_EXECUTABLE)

