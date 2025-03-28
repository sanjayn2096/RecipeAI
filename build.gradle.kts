// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false

}

buildscript {
    dependencies {
        classpath(libs.secrets.gradle.plugin)
        classpath("com.google.gms:google-services:4.3.15") // Ensure this version is up to date
    }
}