############## CameraX ################
-keep class androidx.camera.core.** { *; }
-keep class androidx.camera.camera2.** { *; }
-keep class androidx.camera.lifecycle.** { *; }
-keep class androidx.camera.view.** { *; }
-dontwarn androidx.camera.**

############## ML Kit Barcode Scanner ################
-keep class com.google.mlkit.** { *; }
-dontwarn com.google.mlkit.**
-keep class com.google.android.gms.internal.mlkit_vision_barcode.** { *; }

############## ZXing ################
-keep class com.google.zxing.** { *; }
-keep class com.journeyapps.barcodescanner.** { *; }
-dontwarn com.google.zxing.**
-dontwarn com.journeyapps.barcodescanner.**

############## Accompanist Permissions ################
-dontwarn com.google.accompanist.permissions.**

############## Hilt ################
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class dagger.internal.codegen.** { *; }
-dontwarn dagger.**
-dontwarn javax.inject.**

# Keep generated Hilt code
-keep class * extends dagger.hilt.android.internal.lifecycle.HiltViewModelFactory { *; }
-keep class dagger.hilt.internal.aggregatedroot.codegen.** { *; }
-keep class dagger.hilt.android.internal.lifecycle.HiltViewModelFactory { *; }

############## Room ################
-keep class androidx.room.** { *; }
-keep class * extends androidx.room.RoomDatabase
-keep class * extends androidx.room.RoomOpenHelper
-dontwarn androidx.room.**

############## Jetpack Compose + ViewModel + Navigation ################
-keep class androidx.compose.** { *; }
-keep class androidx.lifecycle.viewmodel.compose.** { *; }
-keep class androidx.navigation.** { *; }
-dontwarn androidx.compose.**

############## Kotlin & Coroutines ################
-keepclassmembers class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.coroutines.**

############## General ################
# Keep your Application class (if custom)
-keep class com.asadbyte.codeapp.MyApplication { *; }

# Optional: keep logs during debug
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}
