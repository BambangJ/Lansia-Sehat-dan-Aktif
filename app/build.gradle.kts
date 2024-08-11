plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.bams.lansiasehataktif"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.bams.lansiasehataktif"
        minSdk = 23
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
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation (libs.retrofit)
    implementation (libs.firebase.config)
    implementation (libs.firebase.database.ktx)
    implementation (libs.logging.interceptor)
    implementation (libs.glide)
    implementation (libs.graphview)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.room.ktx)
    annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0")
    implementation (libs.picasso)
    implementation (libs.play.services.auth)
    implementation (libs.firebase.auth.ktx)
    implementation (libs.converter.gson)
    implementation (platform(libs.firebase.bom))
    implementation (libs.firebase.analytics)
    implementation (libs.play.services.maps)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.crashlytics.buildtools)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}