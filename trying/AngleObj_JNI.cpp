#include "com_physics_AngleObj.h"
#include "AngleObj.h"

/*
 * Class:     com_physics_AngleObj
 * Method:    getV
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_physics_AngleObj_getV(JNIEnv *env, jobject obj)
{
    // Get the native handle
    jclass cls = env->GetObjectClass(obj);
    jfieldID handleField = env->GetFieldID(cls, "nativeHandle", "J");
    jlong handle = env->GetLongField(obj, handleField);

    // Cast to native AngleObj and invoke getV
    AngleObj *angleObj = reinterpret_cast<AngleObj *>(handle);
    return angleObj->getV();
}

/*
 * Class:     com_physics_AngleObj
 * Method:    updateV
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_physics_AngleObj_updateV(JNIEnv *env, jobject obj, jint v)
{
    // Get the native handle
    jclass cls = env->GetObjectClass(obj);
    jfieldID handleField = env->GetFieldID(cls, "nativeHandle", "J");
    jlong handle = env->GetLongField(obj, handleField);

    // Cast to native AngleObj and invoke updateV
    AngleObj *angleObj = reinterpret_cast<AngleObj *>(handle);
    angleObj->updateV(v);
}

/*
 * Class:     com_physics_AngleObj
 * Method:    getXacc
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_physics_AngleObj_getXacc(JNIEnv *env, jobject obj)
{
    jclass cls = env->GetObjectClass(obj);
    jfieldID handleField = env->GetFieldID(cls, "nativeHandle", "J");
    jlong handle = env->GetLongField(obj, handleField);

    AngleObj *angleObj = reinterpret_cast<AngleObj *>(handle);
    return angleObj->getXacc();
}

/*
 * Class:     com_physics_AngleObj
 * Method:    getYacc
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_physics_AngleObj_getYacc(JNIEnv *env, jobject obj)
{
    jclass cls = env->GetObjectClass(obj);
    jfieldID handleField = env->GetFieldID(cls, "nativeHandle", "J");
    jlong handle = env->GetLongField(obj, handleField);

    AngleObj *angleObj = reinterpret_cast<AngleObj *>(handle);
    return angleObj->getYacc();
}

/*
 * Class:     com_physics_AngleObj
 * Method:    updateAcc
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_com_physics_AngleObj_updateAcc(JNIEnv *env, jobject obj, jint x, jint y)
{
    jclass cls = env->GetObjectClass(obj);
    jfieldID handleField = env->GetFieldID(cls, "nativeHandle", "J");
    jlong handle = env->GetLongField(obj, handleField);

    AngleObj *angleObj = reinterpret_cast<AngleObj *>(handle);
    angleObj->updateAcc(x, y);
}

/*
 * Class:     com_physics_AngleObj
 * Method:    getAngle
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_physics_AngleObj_getAngle(JNIEnv *env, jobject obj)
{
    jclass cls = env->GetObjectClass(obj);
    jfieldID handleField = env->GetFieldID(cls, "nativeHandle", "J");
    jlong handle = env->GetLongField(obj, handleField);

    AngleObj *angleObj = reinterpret_cast<AngleObj *>(handle);
    return angleObj->getAngle();
}

/*
 * Class:     com_physics_AngleObj
 * Method:    getOri
 * Signature: ()D
 */
JNIEXPORT jdouble JNICALL Java_com_physics_AngleObj_getOri(JNIEnv *env, jobject obj)
{
    jclass cls = env->GetObjectClass(obj);
    jfieldID handleField = env->GetFieldID(cls, "nativeHandle", "J");
    jlong handle = env->GetLongField(obj, handleField);

    AngleObj *angleObj = reinterpret_cast<AngleObj *>(handle);
    return angleObj->getOri();
}

/*
 * Class:     com_physics_AngleObj
 * Method:    destroyNativeObject
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_physics_AngleObj_destroyNativeObject(JNIEnv *env, jobject obj, jlong handle)
{
    AngleObj *angleObj = reinterpret_cast<AngleObj *>(handle);
    delete angleObj;
}
