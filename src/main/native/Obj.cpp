// Obj.cpp
#include "Obj.h"
#include <jni.h>
#include "com_physics_Obj.h"

extern "C"
{

    // Helper functions to get the nativeHandle
    jlong getNativeHandle(JNIEnv *env, jobject obj)
    {
        jclass cls = env->GetObjectClass(obj);
        jfieldID fid = env->GetFieldID(cls, "nativeHandle", "J");
        return env->GetLongField(obj, fid);
    }

    JNIEXPORT jdouble JNICALL Java_com_physics_Obj_getX(JNIEnv *env, jobject obj)
    {
        Obj *nativeObj = reinterpret_cast<Obj *>(getNativeHandle(env, obj));
        return nativeObj->getX();
    }

    JNIEXPORT jdouble JNICALL Java_com_physics_Obj_getY(JNIEnv *env, jobject obj)
    {
        Obj *nativeObj = reinterpret_cast<Obj *>(getNativeHandle(env, obj));
        return nativeObj->getY();
    }

    JNIEXPORT void JNICALL Java_com_physics_Obj_updateX(JNIEnv *env, jobject obj, jdouble x)
    {
        Obj *nativeObj = reinterpret_cast<Obj *>(getNativeHandle(env, obj));
        nativeObj->updateX(x);
    }

    JNIEXPORT void JNICALL Java_com_physics_Obj_updateY(JNIEnv *env, jobject obj, jdouble y)
    {
        Obj *nativeObj = reinterpret_cast<Obj *>(getNativeHandle(env, obj));
        nativeObj->updateY(y);
    }

    JNIEXPORT jdouble JNICALL Java_com_physics_Obj_getOuterR(JNIEnv *env, jobject obj)
    {
        Obj *nativeObj = reinterpret_cast<Obj *>(getNativeHandle(env, obj));
        return nativeObj->getOuterR();
    }

    JNIEXPORT jdouble JNICALL Java_com_physics_Obj_getInnerR(JNIEnv *env, jobject obj)
    {
        Obj *nativeObj = reinterpret_cast<Obj *>(getNativeHandle(env, obj));
        return nativeObj->getInnerR();
    }

    JNIEXPORT jdouble JNICALL Java_com_physics_Obj_getState(JNIEnv *env, jobject obj)
    {
        Obj *nativeObj = reinterpret_cast<Obj *>(getNativeHandle(env, obj));
        return nativeObj->getState();
    }

    JNIEXPORT jdouble JNICALL Java_com_physics_Obj_getMass(JNIEnv *env, jobject obj)
    {
        Obj *nativeObj = reinterpret_cast<Obj *>(getNativeHandle(env, obj));
        return nativeObj->getMass();
    }

    JNIEXPORT void JNICALL Java_com_physics_Obj_changeState(JNIEnv *env, jobject obj)
    {
        Obj *nativeObj = reinterpret_cast<Obj *>(getNativeHandle(env, obj));
        nativeObj->changeState();
    }

    JNIEXPORT void JNICALL Java_com_physics_Obj_destroyNativeObject(JNIEnv *env, jobject obj, jlong nativeHandle)
    {
        Obj *nativeObj = reinterpret_cast<Obj *>(nativeHandle);
        delete nativeObj;
    }
}
