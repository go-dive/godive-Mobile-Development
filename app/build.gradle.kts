plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.dicoding.godive"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.dicoding.godive"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // TensorFlow Lite
    implementation(libs.tensorflow.lite.metadata)
    implementation(libs.play.services.tflite.support)
    implementation(libs.play.services.tflite.gpu)
    implementation(libs.tensorflow.lite.task.vision.play.services)
    implementation(libs.tensorflow.lite.gpu)
    implementation(libs.tensorflow.lite.v290)



    // Other dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.material.v190)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Coroutine
    implementation(libs.kotlinx.coroutines.android)

    // Retrofit and Glide
    implementation(libs.glide)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    // Room and ViewModel
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    implementation(libs.androidx.room.runtime.v250)  // Pastikan menggunakan versi terbaru
    implementation(libs.androidx.room.ktx.v250)  // Menambahkan room-ktx untuk coroutine
    kapt(libs.androidx.room.compiler.v250)
}
