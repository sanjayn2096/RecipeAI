plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("org.jetbrains.kotlin.kapt")
    id ("com.google.gms.google-services")
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.sunj.recipeai"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.sunj.recipeai"
        minSdk = 26
        targetSdk = 34
        versionCode = 3
        versionName = "3.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
        }
    }
    viewBinding {
        enable = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation (libs.dagger)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.material)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.espresso.core)
    implementation(libs.androidx.espresso.core)
    implementation(libs.firebase.auth.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation ("com.aallam.openai:openai-client:3.0.0")
    implementation ("io.ktor:ktor-client-android:2.2.4")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.0")
    implementation(libs.androidx.datastore.preferences)
    implementation (libs.androidx.navigation.fragment.ktx)
    implementation (libs.androidx.navigation.ui.ktx)
    implementation(libs.generativeai)
    implementation (libs.kotlinx.coroutines.android)
    implementation (libs.gson)
    implementation (libs.glide)
    implementation (libs.lottie.v340)
    implementation (libs.androidx.ui.tooling.preview)
    implementation (libs.androidx.lifecycle.viewmodel.compose)
    implementation (libs.androidx.activity.compose)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    debugImplementation (libs.leakcanary.android)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.material3)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    kapt (libs.androidx.room.compiler.v261)
    implementation ("androidx.compose.material:material-icons-extended:1.7.6")
    // Jetpack Compose Navigation (for bottom bar & profile screen)
    implementation("androidx.navigation:navigation-compose:2.7.7")
// Lifecycle runtime for Jetpack Compose
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
// Coil (for loading user avatar images)
    implementation("io.coil-kt:coil-compose:2.6.0")
}