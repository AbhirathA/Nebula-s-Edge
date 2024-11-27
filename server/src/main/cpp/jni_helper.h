// jni_helper.h
#ifndef JNI_HELPER_H
#define JNI_HELPER_H

#include <jni.h>

inline jlong getNativeHandle(JNIEnv *env, jobject obj)
{
    jclass cls = env->GetObjectClass(obj);
    jfieldID fid = env->GetFieldID(cls, "nativeHandle", "J");
    if (fid == NULL)
    {
        return 0;
    }
    return env->GetLongField(obj, fid);
}

#endif
