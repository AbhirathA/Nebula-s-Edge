/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_physics_LinearObject */

#ifndef _Included_com_physics_LinearObject
#define _Included_com_physics_LinearObject
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_physics_LinearObject
 * Method:    getvX
 * Signature: ()D
 */
JNIEXPORT jdouble JNICALL Java_com_physics_LinearObject_getvX
  (JNIEnv *, jobject);

/*
 * Class:     com_physics_LinearObject
 * Method:    getvY
 * Signature: ()D
 */
JNIEXPORT jdouble JNICALL Java_com_physics_LinearObject_getvY
  (JNIEnv *, jobject);

/*
 * Class:     com_physics_LinearObject
 * Method:    updateV
 * Signature: (DD)V
 */
JNIEXPORT void JNICALL Java_com_physics_LinearObject_updateV
  (JNIEnv *, jobject, jdouble, jdouble);

/*
 * Class:     com_physics_LinearObject
 * Method:    getXacc
 * Signature: ()D
 */
JNIEXPORT jdouble JNICALL Java_com_physics_LinearObject_getXacc
  (JNIEnv *, jobject);

/*
 * Class:     com_physics_LinearObject
 * Method:    getYacc
 * Signature: ()D
 */
JNIEXPORT jdouble JNICALL Java_com_physics_LinearObject_getYacc
  (JNIEnv *, jobject);

/*
 * Class:     com_physics_LinearObject
 * Method:    updateAcc
 * Signature: (DD)V
 */
JNIEXPORT void JNICALL Java_com_physics_LinearObject_updateAcc
  (JNIEnv *, jobject, jdouble, jdouble);

/*
 * Class:     com_physics_LinearObject
 * Method:    getOri
 * Signature: ()D
 */
JNIEXPORT jdouble JNICALL Java_com_physics_LinearObject_getOri
  (JNIEnv *, jobject);

/*
 * Class:     com_physics_LinearObject
 * Method:    checkXTerminal
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_physics_LinearObject_checkXTerminal
  (JNIEnv *, jobject);

/*
 * Class:     com_physics_LinearObject
 * Method:    checkYTerminal
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_physics_LinearObject_checkYTerminal
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif
