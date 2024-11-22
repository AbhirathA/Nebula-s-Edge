// jni_helper.h
#ifndef JNI_HELPER_H
#define JNI_HELPER_H

#include <jni.h>

// Inline implementation to avoid duplicate symbols
inline jlong getNativeHandle(JNIEnv *env, jobject obj)
{
    jclass cls = env->GetObjectClass(obj);
    jfieldID fid = env->GetFieldID(cls, "nativeHandle", "J");
    return env->GetLongField(obj, fid);
}

inline void setNativeHandle(JNIEnv *env, jobject obj, jlong value)
{
    jclass cls = env->GetObjectClass(obj);
    jfieldID fid = env->GetFieldID(cls, "nativeHandle", "J");
    env->SetLongField(obj, fid, value);
}

#endif // JNI_HELPER_H
