plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.dicoding.picodiploma.loginwithanimation"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.dicoding.picodiploma.loginwithanimation"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        named("release") {
            isMinifyEnabled = false
            setProguardFiles(listOf(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"))
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

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.activity:activity:1.9.3")
    implementation("com.google.android.gms:play-services-maps:19.0.0")
    testImplementation("junit:junit:4.13.2")
    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.paging:paging-runtime-ktx:3.1.0")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.activity:activity-ktx:1.9.3")
    implementation ("com.squareup.okhttp3:okhttp:4.10.0") // Ganti dengan versi terbaru
    implementation ("com.squareup.retrofit2:retrofit:2.9.0") // Ganti dengan versi terbaru
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation ("com.github.bumptech.glide:glide:4.14.2") // Ganti dengan versi terbaru
    annotationProcessor ("com.github.bumptech.glide:compiler:4.14.2") // Ganti dengan versi terbaru
    // AndroidX Core Testing for InstantTaskExecutorRule
    testImplementation ("androidx.arch.core:core-testing:2.1.0")

    // Kotlin Coroutines Test for TestDispatcher
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.1")

    // Mockito Core for mocking
    testImplementation ("org.mockito:mockito-core:5.6.0")

    // Mockito Inline for mocking final classes and methods
    testImplementation ("org.mockito:mockito-inline:5.2.0")

    // AndroidTest dependencies
    androidTestImplementation ("androidx.arch.core:core-testing:2.1.0") // InstantTaskExecutorRule
    androidTestImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.1") // TestDispatcher

    testImplementation ("androidx.paging:paging-testing:3.3.5")

    implementation ("com.google.android.gms:play-services-location:21.3.0")
}