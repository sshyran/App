LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_SRC_FILES:= uncpio.c
					                   
LOCAL_MODULE := uncpio
LOCAL_CFLAGS := -I$(LOCAL_PATH) -pie
include $(BUILD_EXECUTABLE)

include $(CLEAR_VARS)

LOCAL_SRC_FILES:= mkcpio.c
					                   
LOCAL_MODULE:= mkcpio
LOCAL_CFLAGS := -I$(LOCAL_PATH) -pie
include $(BUILD_EXECUTABLE)
