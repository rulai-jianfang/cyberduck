/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class ch_cyberduck_core_local_LaunchServicesApplicationFinder */

#ifndef _Included_ch_cyberduck_core_local_LaunchServicesApplicationFinder
#define _Included_ch_cyberduck_core_local_LaunchServicesApplicationFinder
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     ch_cyberduck_core_local_LaunchServicesApplicationFinder
 * Method:    find
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_ch_cyberduck_core_local_LaunchServicesApplicationFinder_findForType
  (JNIEnv *, jobject, jstring);

/*
 * Class:     ch_cyberduck_core_local_LaunchServicesApplicationFinder
 * Method:    findAll
 * Signature: (Ljava/lang/String;)[Ljava/lang/String;
 */
JNIEXPORT jobjectArray JNICALL Java_ch_cyberduck_core_local_LaunchServicesApplicationFinder_findAllForType
  (JNIEnv *, jobject, jstring);

#ifdef __cplusplus
}
#endif
#endif
