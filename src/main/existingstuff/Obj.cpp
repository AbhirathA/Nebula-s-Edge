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

    /*
     * Class:     com_physics_Obj
     * Method:    getX
     * Signature: ()D
     */
    JNIEXPORT jdouble JNICALL Java_com_physics_Obj_getX(JNIEnv *env, jobject obj)
    {
        Obj *nativeObj = reinterpret_cast<Obj *>(getNativeHandle(env, obj));
        return nativeObj->getX();
    }

    /*
     * Class:     com_physics_Obj
     * Method:    getY
     * Signature: ()D
     */
    JNIEXPORT jdouble JNICALL Java_com_physics_Obj_getY(JNIEnv *env, jobject obj)
    {
        Obj *nativeObj = reinterpret_cast<Obj *>(getNativeHandle(env, obj));
        return nativeObj->getY();
    }

    /*
     * Class:     com_physics_Obj
     * Method:    updateX
     * Signature: (D)V
     */
    JNIEXPORT void JNICALL Java_com_physics_Obj_updateX(JNIEnv *env, jobject obj, jdouble x)
    {
        Obj *nativeObj = reinterpret_cast<Obj *>(getNativeHandle(env, obj));
        nativeObj->updateX(x);
    }

    /*
     * Class:     com_physics_Obj
     * Method:    updateY
     * Signature: (D)V
     */
    JNIEXPORT void JNICALL Java_com_physics_Obj_updateY(JNIEnv *env, jobject obj, jdouble y)
    {
        Obj *nativeObj = reinterpret_cast<Obj *>(getNativeHandle(env, obj));
        nativeObj->updateY(y);
    }

    /*
     * Class:     com_physics_Obj
     * Method:    getOuterR
     * Signature: ()D
     */
    JNIEXPORT jdouble JNICALL Java_com_physics_Obj_getOuterR(JNIEnv *env, jobject obj)
    {
        Obj *nativeObj = reinterpret_cast<Obj *>(getNativeHandle(env, obj));
        return nativeObj->getOuterR();
    }

    /*
     * Class:     com_physics_Obj
     * Method:    getInnerR
     * Signature: ()D
     */
    JNIEXPORT jdouble JNICALL Java_com_physics_Obj_getInnerR(JNIEnv *env, jobject obj)
    {
        Obj *nativeObj = reinterpret_cast<Obj *>(getNativeHandle(env, obj));
        return nativeObj->getInnerR();
    }

    /*
     * Class:     com_physics_Obj
     * Method:    getState
     * Signature: ()D
     */
    JNIEXPORT jdouble JNICALL Java_com_physics_Obj_getState(JNIEnv *env, jobject obj)
    {
        Obj *nativeObj = reinterpret_cast<Obj *>(getNativeHandle(env, obj));
        return nativeObj->getState();
    }

    /*
     * Class:     com_physics_Obj
     * Method:    getMass
     * Signature: ()D
     */
    JNIEXPORT jdouble JNICALL Java_com_physics_Obj_getMass(JNIEnv *env, jobject obj)
    {
        Obj *nativeObj = reinterpret_cast<Obj *>(getNativeHandle(env, obj));
        return nativeObj->getMass();
    }

    /*
     * Class:     com_physics_Obj
     * Method:    changeState
     * Signature: ()V
     */
    JNIEXPORT void JNICALL Java_com_physics_Obj_changeState(JNIEnv *env, jobject obj)
    {
        Obj *nativeObj = reinterpret_cast<Obj *>(getNativeHandle(env, obj));
        nativeObj->changeState();
    }

    /*
     * Class:     com_physics_Obj
     * Method:    destroyNativeObject
     * Signature: (J)V
     */
    JNIEXPORT void JNICALL Java_com_physics_Obj_destroyNativeObject(JNIEnv *env, jobject obj, jlong nativeHandle)
    {
        Obj *nativeObj = reinterpret_cast<Obj *>(nativeHandle);
        delete nativeObj;
    }

} // extern "C"
