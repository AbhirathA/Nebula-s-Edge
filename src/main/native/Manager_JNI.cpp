#include "jni_helper.h"
#include "com_physics_Manager.h"
#include "Manager.h"
#include <jni.h>
#include <vector>
#include <map>

#ifdef __cplusplus
extern "C"
{
#endif

    JNIEXPORT jlong JNICALL Java_com_physics_Manager_createNativeObject(JNIEnv *env, jobject obj, jint accX, jint accY, jint lft, jint rt, jint tp, jint bt, jint t)
    {
        Manager *nativeManager = new Manager(accX, accY, lft, rt, tp, bt, t);
        return reinterpret_cast<jlong>(nativeManager);
    }

    JNIEXPORT void JNICALL Java_com_physics_Manager_destroyNativeObject(JNIEnv *env, jobject obj, jlong nativeHandle)
    {
        Manager *nativeManager = reinterpret_cast<Manager *>(nativeHandle);
        delete nativeManager;
    }

    JNIEXPORT void JNICALL Java_com_physics_Manager_update(JNIEnv *env, jobject obj)
    {
        Manager *nativeManager = reinterpret_cast<Manager *>(getNativeHandle(env, obj));
        if (nativeManager != nullptr)
        {
            nativeManager->update();
        }
    }

    JNIEXPORT jint JNICALL Java_com_physics_Manager_dropUser(JNIEnv *env, jobject obj, jint x, jint y, jint peakV, jint driftV, jint angle, jint thrust, jint thrustPersistance,
                                                             jint movePersistance, jint coolDown, jint accX, jint accY, jint innerRad, jint outerRad, jint mass,
                                                             jint health, jint bulletSpeed, jint bulletLife)
    {
        Manager *nativeManager = reinterpret_cast<Manager *>(getNativeHandle(env, obj));
        if (nativeManager != nullptr)
        {
            int result = nativeManager->dropUser(x, y, peakV, driftV, angle, thrust, thrustPersistance,
                                                 movePersistance, coolDown, accX, accY, innerRad, outerRad, mass, health, bulletSpeed, bulletLife);
            return static_cast<jint>(result);
        }
        return -1;
    }

    JNIEXPORT void JNICALL Java_com_physics_Manager_forward(JNIEnv *env, jobject obj, jint id)
    {
        Manager *nativeManager = reinterpret_cast<Manager *>(getNativeHandle(env, obj));
        if (nativeManager != nullptr)
        {
            nativeManager->forward(id);
        }
    }

    JNIEXPORT void JNICALL Java_com_physics_Manager_stop(JNIEnv *env, jobject obj, jint id)
    {
        Manager *nativeManager = reinterpret_cast<Manager *>(getNativeHandle(env, obj));
        if (nativeManager != nullptr)
        {
            nativeManager->stop(id);
        }
    }

    JNIEXPORT void JNICALL Java_com_physics_Manager_thrust(JNIEnv *env, jobject obj, jint id)
    {
        Manager *nativeManager = reinterpret_cast<Manager *>(getNativeHandle(env, obj));
        if (nativeManager != nullptr)
        {
            nativeManager->thrust(id);
        }
    }

    JNIEXPORT void JNICALL Java_com_physics_Manager_left(JNIEnv *env, jobject obj, jint id)
    {
        Manager *nativeManager = reinterpret_cast<Manager *>(getNativeHandle(env, obj));
        if (nativeManager != nullptr)
        {
            nativeManager->left(id);
        }
    }

    JNIEXPORT void JNICALL Java_com_physics_Manager_right(JNIEnv *env, jobject obj, jint id)
    {
        Manager *nativeManager = reinterpret_cast<Manager *>(getNativeHandle(env, obj));
        if (nativeManager != nullptr)
        {
            nativeManager->right(id);
        }
    }

    JNIEXPORT void JNICALL Java_com_physics_Manager_removeDead(JNIEnv *env, jobject obj, jintArray idsArray)
    {
        // Manager *nativeManager = reinterpret_cast<Manager *>(getNativeHandle(env, obj));
        // if (nativeManager != nullptr)
        {
          //  jint *ids = env->GetIntArrayElements(idsArray, NULL);
          //  jsize length = env->GetArrayLength(idsArray);
          //  std::vector<int> idsVector(ids, ids + length);
          //  env->ReleaseIntArrayElements(idsArray, ids, 0);
          //  nativeManager->removeDead(idsVector);
        }
    }

    JNIEXPORT jint JNICALL Java_com_physics_Manager_shoot(JNIEnv *env, jobject obj, jint id, jint innerRadius, jint outerRadius, jint mass)
    {
        Manager *nativeManager = reinterpret_cast<Manager *>(getNativeHandle(env, obj));
        if (nativeManager != nullptr)
        {
            int result = nativeManager->shoot(id, innerRadius, outerRadius, mass);
            return static_cast<jint>(result);
        }
        return -1;
    }

    JNIEXPORT jint JNICALL Java_com_physics_Manager_dropAsteroid(JNIEnv *env, jobject obj, jint x, jint y, jint innerRad, jint outerRad, jint mass)
    {
        Manager *nativeManager = reinterpret_cast<Manager *>(getNativeHandle(env, obj));
        if (nativeManager != nullptr)
        {
            int result = nativeManager->dropAsteroid(x, y, innerRad, outerRad, mass);
            return static_cast<jint>(result);
        }
        return -1;
    }

    JNIEXPORT jint JNICALL Java_com_physics_Manager_dropBlackHole(JNIEnv *env, jobject obj, jint x, jint y, jint innerRad, jint outerRad, jint mass)
    {
        Manager *nativeManager = reinterpret_cast<Manager *>(getNativeHandle(env, obj));
        if (nativeManager != nullptr)
        {
            int result = nativeManager->dropBlackHole(x, y, innerRad, outerRad, mass);
            return static_cast<jint>(result);
        }
        return -1;
    }

    JNIEXPORT jint JNICALL Java_com_physics_Manager_dropEnemy(JNIEnv *env, jobject obj, jint x, jint y, jint v, jint res, jint innerRad, jint outerRad, jint mass, jboolean startX, jint startSign, jlong aim)
    {
        Manager *nativeManager = reinterpret_cast<Manager *>(getNativeHandle(env, obj));
        if (nativeManager != nullptr)
        {
            int result = nativeManager->dropEnemy(x, y, v, res, innerRad, outerRad, mass, startX, startSign, reinterpret_cast<Obj *>(aim));
            return static_cast<jint>(result);
        }
        return -1;
    }

    JNIEXPORT jint JNICALL Java_com_physics_Manager_dropMeteor(JNIEnv *env, jobject obj, jint x, jint y, jint vX, jint vY, jint accX, jint accY, jint innerRad, jint outerRad, jint mass)
    {
        Manager *nativeManager = reinterpret_cast<Manager *>(getNativeHandle(env, obj));
        if (nativeManager != nullptr)
        {
            int result = nativeManager->dropMeteor(x, y, vX, vY, accX, accY, innerRad, outerRad, mass);
            return static_cast<jint>(result);
        }
        return -1;
    }

    JNIEXPORT jobjectArray JNICALL Java_com_physics_Manager_display(JNIEnv *env, jobject obj, jint lowerX, jint lowerY, jint upperX, jint upperY)
    {
        Manager *nativeManager = reinterpret_cast<Manager *>(getNativeHandle(env, obj));
        if (nativeManager != nullptr)
        {
            std::vector<std::vector<int>> data = nativeManager->display(lowerX, lowerY, upperX, upperY);

            jsize outerSize = data.size();
            jclass intArrayClass = env->FindClass("[I");
            if (intArrayClass == NULL)
            {
                return NULL;
            }

            jobjectArray outerArray = env->NewObjectArray(outerSize, intArrayClass, NULL);
            if (outerArray == NULL)
            {
                return NULL;
            }

            for (jsize i = 0; i < outerSize; i++)
            {
                std::vector<int> &innerVector = data[i];
                jsize innerSize = innerVector.size();

                jintArray innerArray = env->NewIntArray(innerSize);
                if (innerArray == NULL)
                {
                    return NULL;
                }

                env->SetIntArrayRegion(innerArray, 0, innerSize, innerVector.data());
                env->SetObjectArrayElement(outerArray, i, innerArray);

                env->DeleteLocalRef(innerArray);
            }

            return outerArray;
        }
        return NULL;
    }

#ifdef __cplusplus
}
#endif
