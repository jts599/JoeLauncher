# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Keep Kotlinx Serialization classes and annotations
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

# Keep all kotlinx.serialization classes
-keep class kotlinx.serialization.** { *; }
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep serializers for @Serializable classes
-if @kotlinx.serialization.Serializable class **
-keepnames class <1>$$serializer {
    <1>$$serializer INSTANCE;
}

# Keep the serializer and companion of serializable classes
-if @kotlinx.serialization.Serializable class ** {
    static **$Companion Companion;
}
-keepnames class <1>$Companion {
    kotlinx.serialization.KSerializer serializer(...);
}

-keepclassmembers class **$WhenMappings {
    <fields>;
}

# Keep BackgroundSettings for serialization
-keep class app.joelauncher.helper.BackgroundSettings { *; }

# Keep all @Serializable classes
-keep @kotlinx.serialization.Serializable class * { *; }