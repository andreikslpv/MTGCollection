// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {

    val javaVersion by extra(JavaVersion.VERSION_17)
    val jvmTarget by extra("17")

    dependencies {
        classpath(libs.google.services)
        classpath(libs.gradle)
        classpath(libs.firebase.crashlytics.gradle)
    }
}

plugins {
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
}