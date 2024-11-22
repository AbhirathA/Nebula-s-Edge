// FixedObj_JNI.cpp
#include "jni_helper.h"
#include "com_physics_FixedObj.h"
#include "FixedObj.h"
#include "LinearObj.h"
#include "Obj.h"
#include <jni.h>

#ifdef __cplusplus
extern "C"
{
#endif

    JNIEXPORT jlong JNICALL Java_com_physics_FixedObj_createNativeObject(JNIEnv *env, jclass cls, jint id, jint x, jint y, jint innerRad, jint outerRad, jint mass)
    {
        FixedObj *nativeObj = new FixedObj(id, x, y, innerRad, outerRad, mass);
        return reinterpret_cast<jlong>(nativeObj);
    }

    JNIEXPORT void JNICALL Java_com_physics_FixedObj_destroyNativeObject(JNIEnv *env, jobject obj, jlong nativeHandle)
    {
        FixedObj *nativeObj = reinterpret_cast<FixedObj *>(nativeHandle);
        delete nativeObj;
    }

    JNIEXPORT jboolean JNICALL Java_com_physics_FixedObj_checkCollision(JNIEnv *env, jobject obj, jobject otherObj)
    {
        FixedObj *nativeObj = reinterpret_cast<FixedObj *>(getNativeHandle(env, obj));
        Obj *otherNativeObj = reinterpret_cast<Obj *>(getNativeHandle(env, otherObj));
        bool result = nativeObj->checkCollision(otherNativeObj);
        return result ? JNI_TRUE : JNI_FALSE;
    }

    JNIEXPORT void JNICALL Java_com_physics_FixedObj_updatePos(JNIEnv *env, jobject obj, jint t)
    {
        FixedObj *nativeObj = reinterpret_cast<FixedObj *>(getNativeHandle(env, obj));
        nativeObj->updatePos(t);
    }

    JNIEXPORT jint JNICALL Java_com_physics_FixedObj_getNextX(JNIEnv *env, jobject obj, jint t)
    {
        FixedObj *nativeObj = reinterpret_cast<FixedObj *>(getNativeHandle(env, obj));
        int nextX = nativeObj->getNextX(t);
        return static_cast<jint>(nextX);
    }

    JNIEXPORT jint JNICALL Java_com_physics_FixedObj_getNextY(JNIEnv *env, jobject obj, jint t)
    {
        FixedObj *nativeObj = reinterpret_cast<FixedObj *>(getNativeHandle(env, obj));
        int nextY = nativeObj->getNextY(t);
        return static_cast<jint>(nextY);
    }

    JNIEXPORT jboolean JNICALL Java_com_physics_FixedObj_boundCorrection(JNIEnv *env, jobject obj, jint lft, jint rt, jint tp, jint bt, jint t)
    {
        FixedObj *nativeObj = reinterpret_cast<FixedObj *>(getNativeHandle(env, obj));
        bool result = nativeObj->boundCorrection(lft, rt, tp, bt, t);
        return result ? JNI_TRUE : JNI_FALSE;
    }

    JNIEXPORT jboolean JNICALL Java_com_physics_FixedObj_collisionCorection(JNIEnv *env, jobject obj, jobject otherObj)
    {
        FixedObj *nativeObj = reinterpret_cast<FixedObj *>(getNativeHandle(env, obj));
        Obj *otherNativeObj = reinterpret_cast<Obj *>(getNativeHandle(env, otherObj));
        bool result = nativeObj->collisionCorection(otherNativeObj);
        return result ? JNI_TRUE : JNI_FALSE;
    }

#ifdef __cplusplus
}
#endif
