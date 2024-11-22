#include "LinearObj.h"

#include "com_physics_LinearObject.h"
#include <jni.h>

jlong getNativeHandle(JNIEnv *env, jobject obj);
void setNativeHandle(JNIEnv *env, jobject obj, jlong value);

extern "C"
{

    // getvX
    JNIEXPORT jdouble JNICALL Java_com_physics_LinearObject_getvX(JNIEnv *env, jobject obj)
    {
        LinearObj *nativeObj = reinterpret_cast<LinearObj *>(getNativeHandle(env, obj));
        return nativeObj->getvX();
    }

    // getvY
    JNIEXPORT jdouble JNICALL Java_com_physics_LinearObject_getvY(JNIEnv *env, jobject obj)
    {
        LinearObj *nativeObj = reinterpret_cast<LinearObj *>(getNativeHandle(env, obj));
        return nativeObj->getvY();
    }

    // updateV
    JNIEXPORT void JNICALL Java_com_physics_LinearObject_updateV(JNIEnv *env, jobject obj, jdouble x, jdouble y)
    {
        LinearObj *nativeObj = reinterpret_cast<LinearObj *>(getNativeHandle(env, obj));
        nativeObj->updateV(x, y);
    }

    // getXacc
    JNIEXPORT jdouble JNICALL Java_com_physics_LinearObject_getXacc(JNIEnv *env, jobject obj)
    {
        LinearObj *nativeObj = reinterpret_cast<LinearObj *>(getNativeHandle(env, obj));
        return nativeObj->getXacc();
    }

    // getYacc
    JNIEXPORT jdouble JNICALL Java_com_physics_LinearObject_getYacc(JNIEnv *env, jobject obj)
    {
        LinearObj *nativeObj = reinterpret_cast<LinearObj *>(getNativeHandle(env, obj));
        return nativeObj->getYacc();
    }

    // updateAcc
    JNIEXPORT void JNICALL Java_com_physics_LinearObject_updateAcc(JNIEnv *env, jobject obj, jdouble x, jdouble y)
    {
        LinearObj *nativeObj = reinterpret_cast<LinearObj *>(getNativeHandle(env, obj));
        nativeObj->updateAcc(x, y);
    }

    // getOri
    JNIEXPORT jdouble JNICALL Java_com_physics_LinearObject_getOri(JNIEnv *env, jobject obj)
    {
        LinearObj *nativeObj = reinterpret_cast<LinearObj *>(getNativeHandle(env, obj));
        return nativeObj->getOri();
    }

    // checkXTerminal
    JNIEXPORT jboolean JNICALL Java_com_physics_LinearObject_checkXTerminal(JNIEnv *env, jobject obj)
    {
        LinearObj *nativeObj = reinterpret_cast<LinearObj *>(getNativeHandle(env, obj));
        return nativeObj->checkXTerminal();
    }

    // checkYTerminal
    JNIEXPORT jboolean JNICALL Java_com_physics_LinearObject_checkYTerminal(JNIEnv *env, jobject obj)
    {
        LinearObj *nativeObj = reinterpret_cast<LinearObj *>(getNativeHandle(env, obj));
        return nativeObj->checkYTerminal();
    }

} // extern "C"