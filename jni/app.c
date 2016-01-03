#include <jni.h>
#include <stdio.h>
#include <limits.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <crixec_app_imagefactory_utils_NativeUtils.h>
#include <bootimg.h>

char *signcode = "1c04fa3d6448f7e494de7af5f106dc50";

jstring JNICALL Java_crixec_app_imagefactory_utils_NativeUtils_copyright(
		JNIEnv *env, jclass clazz) {
	return (*env)->NewStringUTF(env, "Crixec All Right Reserved!");
}

int cat(char *from, char *to) {
	LOGI("from=%s to=%s\n", from, to);
/*	FILE *fp1 = fopen(from, "rb");
	if(fp1){
		FILE *fp2 = fopen(to, "wb");
		if(fp2){
			unsigned char buf[4096];
			int code = -1;
			while((code = fread(buf, sizeof(unsigned char), 4096, fp1)) > 0)
				fwrite(buf, sizeof(unsigned char), code,fp2);
			fclose(fp1);
			fclose(fp2);
			return 0;
		}
	}*/
	char args[PATH_MAX];
	sprintf(args, "su -c '/system/bin/toolbox cat %s > %s'", from, to);
	LOGI("JNI cat command:%s", args);
	return system(args);
}
jint JNICALL Java_crixec_app_imagefactory_utils_NativeUtils_cat(JNIEnv *env,
		jclass clazz, jobjectArray arr) {
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
	for (; i < length; i++) {
		LOGI("argv[%d]=%s", i, argv[i]);
	}
	if (length != 3) {
		return 1;
	}
	return cat(argv[1], argv[2]);
}

jint JNICALL Java_crixec_app_imagefactory_utils_NativeUtils_checkWorksapce(
		JNIEnv *env, jclass clazz, jstring str) {
	char *path = (*env)->GetStringUTFChars(env, str, 0);
	char tmp[PATH_MAX];
	sprintf(tmp, "/system/bin/sh -c 'toolbox ls %s'", path);
	LOGI("Use Command:%s", tmp);
	int code = system(tmp);
	LOGI("Result Code:%d", code);
	return code;
}
