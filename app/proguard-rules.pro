# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.

# Keep Koin classes
-keep class org.koin.** { *; }
-dontwarn org.koin.**

# Keep Room classes
-keep class androidx.room.** { *; }
-dontwarn androidx.room.**

# Keep Ktor classes
-keep class io.ktor.** { *; }
-dontwarn io.ktor.**

# Keep data classes used in serialization
-keep @kotlinx.serialization.Serializable class com.tripkeun.android.data.remote.dto.** { *; }
-keep class com.arkhe.menu.domain.model.** { *; }

# Keep CoreComponentFactory
-keep class androidx.core.app.CoreComponentFactory { *; }
-dontwarn androidx.core.app.CoreComponentFactory

# Keep ViewModels
-keep class com.arkhe.menu.presentation.viewmodel.** { *; }

# Keep Compose classes
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# Kotlinx Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-dontnote kotlinx.serialization.SerializationKt

-keep,includedescriptorclasses class com.arkhe.menu.**$serializer { *; }
-keepclassmembers class com.arkhe.menu.** {
    *** Companion;
}
-keepclasseswithmembers class com.arkhe.menu.** {
    kotlinx.serialization.KSerializer serializer(...);
}