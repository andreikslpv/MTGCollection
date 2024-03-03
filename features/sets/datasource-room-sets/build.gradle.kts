plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "com.andreikslpv.datasource_room_sets"
    compileSdk = rootProject.extra["targetAndroidSdk"] as Int

    defaultConfig {
        minSdk = rootProject.extra["minAndroidSdk"] as Int

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = rootProject.extra["javaVersion"] as JavaVersion
        targetCompatibility = rootProject.extra["javaVersion"] as JavaVersion
    }
    kotlinOptions {
        jvmTarget = rootProject.extra["kotlinVersion"] as String
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    //Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    kapt(libs.room.compiler)

    api(project(":features:sets:domain-sets"))

    testImplementation(libs.junit)
}