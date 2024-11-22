// LinearObj_JNI.cpp
#include "jni_helper.h"
#include "com_physics_LinearObj.h"
#include "LinearObj.h"
#include <jni.h>

#ifdef __cplusplus
extern "C"
{
#endif

    JNIEXPORT jint JNICALL Java_com_physics_LinearObj_getvX(JNIEnv *env, jobject obj)
    {
        LinearObj *nativeObj = reinterpret_cast<LinearObj *>(getNativeHandle(env, obj));
        return nativeObj->getvX();
    }

    JNIEXPORT jint JNICALL Java_com_physics_LinearObj_getvY(JNIEnv *env, jobject obj)
    {
        LinearObj *nativeObj = reinterpret_cast<LinearObj *>(getNativeHandle(env, obj));
        return nativeObj->getvY();
    }

    JNIEXPORT void JNICALL Java_com_physics_LinearObj_updateV(JNIEnv *env, jobject obj, jint x, jint y)
    {
        LinearObj *nativeObj = reinterpret_cast<LinearObj *>(getNativeHandle(env, obj));
        nativeObj->updateV(x, y);
    }

    JNIEXPORT jint JNICALL Java_com_physics_LinearObj_getXacc(JNIEnv *env, jobject obj)
    {
        LinearObj *nativeObj = reinterpret_cast<LinearObj *>(getNativeHandle(env, obj));
        return nativeObj->getXacc();
    }

    JNIEXPORT jint JNICALL Java_com_physics_LinearObj_getYacc(JNIEnv *env, jobject obj)
    {
        LinearObj *nativeObj = reinterpret_cast<LinearObj *>(getNativeHandle(env, obj));
        return nativeObj->getYacc();
    }

    JNIEXPORT void JNICALL Java_com_physics_LinearObj_updateAcc(JNIEnv *env, jobject obj, jint x, jint y)
    {
        LinearObj *nativeObj = reinterpret_cast<LinearObj *>(getNativeHandle(env, obj));
        nativeObj->updateAcc(x, y);
    }

    JNIEXPORT jdouble JNICALL Java_com_physics_LinearObj_getOri(JNIEnv *env, jobject obj)
    {
        LinearObj *nativeObj = reinterpret_cast<LinearObj *>(getNativeHandle(env, obj));
        return nativeObj->getOri();
    }

    JNIEXPORT jboolean JNICALL Java_com_physics_LinearObj_checkXTerminal(JNIEnv *env, jobject obj)
    {
        LinearObj *nativeObj = reinterpret_cast<LinearObj *>(getNativeHandle(env, obj));
        return nativeObj->checkXTerminal() ? JNI_TRUE : JNI_FALSE;
    }

    JNIEXPORT jboolean JNICALL Java_com_physics_LinearObj_checkYTerminal(JNIEnv *env, jobject obj)
    {
        LinearObj *nativeObj = reinterpret_cast<LinearObj *>(getNativeHandle(env, obj));
        return nativeObj->checkYTerminal() ? JNI_TRUE : JNI_FALSE;
    }

#ifdef __cplusplus
}
#endif
