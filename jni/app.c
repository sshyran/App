#include <jni.h>
#include <stdio.h>
#include <crixec_app_imagefactory_utils_NativeUtils.h>

char *signcode = "1c04fa3d6448f7e494de7af5f106dc50";

jstring JNICALL Java_crixec_app_imagefactory_utils_NativeUtils_copyright(
		JNIEnv *env, jclass clazz) {
	return (*env)->NewStringUTF(env, "Crixec All Right Reserved!");
}

