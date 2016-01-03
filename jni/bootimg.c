#include <stdio.h>
#include <string.h>
#include <malloc.h>
#include <jni.h>
#include <limits.h>
#include <unistd.h>
#include <crixec_app_imagefactory_utils_NativeUtils.h>
#include <bootimg.h>

jint JNICALL Java_crixec_app_imagefactory_utils_NativeUtils_simg2img
  (JNIEnv *env, jclass clazz, jobjectArray arr){
	jint length = (*env)->GetArrayLength(env, arr);
		int i = 0;
		char *argv[length];
		for (; i < length; i++) {
			jstring string = (jstring)((*env)->GetObjectArrayElement(env, arr, i));
		    if (string != NULL) {
		     char *c = (*env)->GetStringUTFChars(env, string, 0);
		     argv[i] = c;
		   }
		}
		i = 0;
		for(;i < length; i++){
			LOGI("argv[%d]=%s", i, argv[i]);
		}
		return simg2img_main(length, argv);
}



jint JNICALL Java_crixec_app_imagefactory_utils_NativeUtils_unpackcpb
  (JNIEnv *env, jclass clazz, jobjectArray arr){
	jint length = (*env)->GetArrayLength(env, arr);
		int i = 0;
		char *argv[length];
		for (; i < length; i++) {
			jstring string = (jstring)((*env)->GetObjectArrayElement(env, arr, i));
		    if (string != NULL) {
		     char *c = (*env)->GetStringUTFChars(env, string, 0);
		     argv[i] = c;
		   }
		}
		i = 0;
		for(;i < length; i++){
			LOGI("argv[%d]=%s", i, argv[i]);
		}
	return unpackcpb_main(length, argv);
}

jint JNICALL Java_crixec_app_imagefactory_utils_NativeUtils_unpackapp
  (JNIEnv *env, jclass clazz, jobjectArray arr){
	jint length = (*env)->GetArrayLength(env, arr);
		int i = 0;
		char *argv[length];
		for (; i < length; i++) {
			jstring string = (jstring)((*env)->GetObjectArrayElement(env, arr, i));
		    if (string != NULL) {
		     char *c = (*env)->GetStringUTFChars(env, string, 0);
		     argv[i] = c;
		   }
		}
		i = 0;
		for(;i < length; i++){
			LOGI("argv[%d]=%s", i, argv[i]);
		}
		return unpackapp_main(length, argv);
}

jint JNICALL Java_crixec_app_imagefactory_utils_NativeUtils_repackbootimg
  (JNIEnv *env, jclass clazz, jobjectArray arr){
	jint length = (*env)->GetArrayLength(env, arr);
		int i = 0;
		char *argv[length];
		for (; i < length; i++) {
			jstring string = (jstring)((*env)->GetObjectArrayElement(env, arr, i));
		    if (string != NULL) {
		     char *c = (*env)->GetStringUTFChars(env, string, 0);
		     argv[i] = c;
		   }
		}
		i = 0;
		for(;i < length; i++){
			LOGI("argv[%d]=%s", i, argv[i]);
		}
		return repackbootimg_main(length, argv);
}

jint JNICALL Java_crixec_app_imagefactory_utils_NativeUtils_unpackbootimg
  (JNIEnv * env, jclass clazz, jobjectArray arr){
	jint length = (*env)->GetArrayLength(env, arr);
		int i = 0;
		char *argv[length];
		for (; i < length; i++) {
			jstring string = (jstring)((*env)->GetObjectArrayElement(env, arr, i));
		    if (string != NULL) {
		     char *c = (*env)->GetStringUTFChars(env, string, 0);
		     argv[i] = c;
		   }
		}
		i = 0;
		for(;i < length; i++){
			LOGI("argv[%d]=%s", i, argv[i]);
		}
		return unpackbootimg_main(length, argv);
}
jint JNICALL Java_crixec_app_imagefactory_utils_NativeUtils_minigzip
  (JNIEnv * env, jclass clazz, jobjectArray arr){
	jint length = (*env)->GetArrayLength(env, arr);
		int i = 0;
		char *argv[length];
		for (; i < length; i++) {
			jstring string = (jstring)((*env)->GetObjectArrayElement(env, arr, i));
		    if (string != NULL) {
		     char *c = (*env)->GetStringUTFChars(env, string, 0);
		     argv[i] = c;
		   }
		}
		i = 0;
		for(;i < length; i++){
			LOGI("argv[%d]=%s", i, argv[i]);
		}
		return minigzip(length, argv);
}
int exist(char *f) {
	if(access(f, R_OK) == 0){
		return 0;
	}	else {
		return 1;
	}
}
