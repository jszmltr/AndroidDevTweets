# Support libraries
-dontwarn android.support.**
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep public class * extends android.support.v4.**

-dontwarn com.squareup.picasso.**


# Ignore Rx warnings (see https://github.com/ReactiveX/RxJava/issues/1415)
-dontwarn rx.internal.util.unsafe.**
-dontwarn rx.observables.**

# Fix reflection issues https://github.com/ReactiveX/RxJava/issues/3097#issuecomment-135125272
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    long producerNode;
    long consumerNode;
}
