// FixedObj.cpp
#include "FixedObj.h"
#include <cmath>
#include "com_physics_FixedObj.h"
#include <jni.h>

jlong getNativeHandle(JNIEnv *env, jobject obj)
{
    jclass cls = env->GetObjectClass(obj);
    jfieldID fid = env->GetFieldID(cls, "nativeHandle", "J");
    return env->GetLongField(obj, fid);
}

void setNativeHandle(JNIEnv *env, jobject obj, jlong value)
{
    jclass cls = env->GetObjectClass(obj);
    jfieldID fid = env->GetFieldID(cls, "nativeHandle", "J");
    env->SetLongField(obj, fid, value);
}

FixedObj::FixedObj(int id, double x, double y, double innerRad, double outerRad, double mass)
    : LinearObj(id, x, y, 0.0, 0.0, 0.0, 0.0, 0.0, innerRad, outerRad, mass) {}

double FixedObj::getNextX(double t)
{
    return this->posX;
}

double FixedObj::getNextY(double t)
{
    return this->posY;
}

void FixedObj::updatePos(double t) {}

bool FixedObj::checkCollision(Obj *obj)
{
    double dx = this->getX() - obj->getX();
    double dy = this->getY() - obj->getY();
    double distance = sqrt(dx * dx + dy * dy);
    double overlap = this->getInnerR() + obj->getInnerR() - distance;

    return overlap > 0;
}

bool FixedObj::boundCorrection(double lft, double rt, double tp, double bt, double t)
{
    return true;
}

bool FixedObj::collisionCorection(Obj *obj)
{
    LinearObj *linearObj = dynamic_cast<LinearObj *>(obj);
    if (linearObj)
    {
        double dx = this->getX() - linearObj->getX();
        double dy = this->getY() - linearObj->getY();
        double distance = sqrt(dx * dx + dy * dy);
        double overlap = this->getInnerR() + linearObj->getInnerR() - distance;

        if (overlap > 0)
        {
            double adjustmentFactor = overlap;
            double adjustmentX = (dx * adjustmentFactor) / distance;
            double adjustmentY = (dy * adjustmentFactor) / distance;

            linearObj->updateX(linearObj->getX() + adjustmentX);
            linearObj->updateY(linearObj->getY() + adjustmentY);

            linearObj->updateV(-linearObj->getvX(), -linearObj->getvY());

            return true;
        }
    }
    return false;
}

extern "C"
{

    JNIEXPORT jlong JNICALL Java_com_physics_FixedObj_createNativeObject(JNIEnv *env, jclass cls, jint id, jdouble x, jdouble y, jdouble innerRad, jdouble outerRad, jdouble mass)
    {
        FixedObj *nativeObj = new FixedObj(id, x, y, innerRad, outerRad, mass);
        return reinterpret_cast<jlong>(nativeObj);
    }

    JNIEXPORT void JNICALL Java_com_physics_FixedObj_destroyNativeObject(JNIEnv *env, jobject obj, jlong nativeHandle)
    {
        FixedObj *nativeObj = reinterpret_cast<FixedObj *>(nativeHandle);
        delete nativeObj;
    }

    JNIEXPORT void JNICALL Java_com_physics_FixedObj_updatePos(JNIEnv *env, jobject obj, jdouble t)
    {
        FixedObj *nativeObj = reinterpret_cast<FixedObj *>(getNativeHandle(env, obj));
        nativeObj->updatePos(t);
    }

    JNIEXPORT jdouble JNICALL Java_com_physics_FixedObj_getNextX(JNIEnv *env, jobject obj, jdouble t)
    {
        FixedObj *nativeObj = reinterpret_cast<FixedObj *>(getNativeHandle(env, obj));
        return nativeObj->getNextX(t);
    }

    JNIEXPORT jdouble JNICALL Java_com_physics_FixedObj_getNextY(JNIEnv *env, jobject obj, jdouble t)
    {
        FixedObj *nativeObj = reinterpret_cast<FixedObj *>(getNativeHandle(env, obj));
        return nativeObj->getNextY(t);
    }

    JNIEXPORT jboolean JNICALL Java_com_physics_FixedObj_checkCollision(JNIEnv *env, jobject obj, jobject otherObj)
    {
        FixedObj *nativeObj = reinterpret_cast<FixedObj *>(getNativeHandle(env, obj));
        Obj *otherNativeObj = reinterpret_cast<Obj *>(getNativeHandle(env, otherObj));
        return nativeObj->checkCollision(otherNativeObj);
    }

    JNIEXPORT jboolean JNICALL Java_com_physics_FixedObj_boundCorrection(JNIEnv *env, jobject obj, jdouble lft, jdouble rt, jdouble tp, jdouble bt, jdouble t)
    {
        FixedObj *nativeObj = reinterpret_cast<FixedObj *>(getNativeHandle(env, obj));
        return nativeObj->boundCorrection(lft, rt, tp, bt, t);
    }

    JNIEXPORT jboolean JNICALL Java_com_physics_FixedObj_collisionCorection(JNIEnv *env, jobject obj, jobject otherObj)
    {
        FixedObj *nativeObj = reinterpret_cast<FixedObj *>(getNativeHandle(env, obj));
        Obj *otherNativeObj = reinterpret_cast<Obj *>(getNativeHandle(env, otherObj));
        return nativeObj->collisionCorection(otherNativeObj);
    }
}