// Obj_JNI.cpp
#include "jni_helper.h"
#include "com_physics_Obj.h"
#include "Obj.h"
#include <jni.h>

#ifdef __cplusplus
extern "C"
{
#endif

    JNIEXPORT void JNICALL Java_com_physics_Obj_destroyNativeObject(JNIEnv *env, jobject obj, jlong nativeHandle)
    {
        Obj *nativeObj = reinterpret_cast<Obj *>(nativeHandle);
        delete nativeObj;
    }

    JNIEXPORT jint JNICALL Java_com_physics_Obj_getX(JNIEnv *env, jobject obj)
    {
        Obj *nativeObj = reinterpret_cast<Obj *>(getNativeHandle(env, obj));
        if (nativeObj == nullptr)
        {
            // Throw an exception or handle the error
            env->ThrowNew(env->FindClass("java/lang/NullPointerException"), "Native object is null in getX");
            return 0;
        }
        return nativeObj->getX();
    }

    JNIEXPORT jint JNICALL Java_com_physics_Obj_getY(JNIEnv *env, jobject obj)
    {
        Obj *nativeObj = reinterpret_cast<Obj *>(getNativeHandle(env, obj));
        return nativeObj->getY();
    }

    JNIEXPORT void JNICALL Java_com_physics_Obj_updateX(JNIEnv *env, jobject obj, jint x)
    {
        Obj *nativeObj = reinterpret_cast<Obj *>(getNativeHandle(env, obj));
        nativeObj->updateX(x);
    }

    JNIEXPORT void JNICALL Java_com_physics_Obj_updateY(JNIEnv *env, jobject obj, jint y)
    {
        Obj *nativeObj = reinterpret_cast<Obj *>(getNativeHandle(env, obj));
        nativeObj->updateY(y);
    }

    JNIEXPORT jint JNICALL Java_com_physics_Obj_getOuterR(JNIEnv *env, jobject obj)
    {
        Obj *nativeObj = reinterpret_cast<Obj *>(getNativeHandle(env, obj));
        return nativeObj->getOuterR();
    }

    JNIEXPORT jint JNICALL Java_com_physics_Obj_getInnerR(JNIEnv *env, jobject obj)
    {
        Obj *nativeObj = reinterpret_cast<Obj *>(getNativeHandle(env, obj));
        return nativeObj->getInnerR();
    }

    JNIEXPORT jint JNICALL Java_com_physics_Obj_getState(JNIEnv *env, jobject obj)
    {
        Obj *nativeObj = reinterpret_cast<Obj *>(getNativeHandle(env, obj));
        return nativeObj->getState();
    }

    JNIEXPORT jint JNICALL Java_com_physics_Obj_getMass(JNIEnv *env, jobject obj)
    {
        Obj *nativeObj = reinterpret_cast<Obj *>(getNativeHandle(env, obj));
        return nativeObj->getMass();
    }

    JNIEXPORT void JNICALL Java_com_physics_Obj_changeState(JNIEnv *env, jobject obj)
    {
        Obj *nativeObj = reinterpret_cast<Obj *>(getNativeHandle(env, obj));
        nativeObj->changeState();
    }

#ifdef __cplusplus
}
#endif
