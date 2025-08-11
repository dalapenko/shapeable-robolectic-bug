plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("io.github.takahirom.roborazzi")
}

android {
    namespace = "tech.dalapenko.shapeablebug"
    compileSdk = 35

    defaultConfig {
        applicationId = "tech.dalapenko.shapeablebug"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures.viewBinding = true

    testOptions.unitTests {
        isIncludeAndroidResources = true
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

    implementation("androidx.core:core-ktx:1.16.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    testImplementation("org.robolectric:robolectric:4.15.1")
    testImplementation("io.github.takahirom.roborazzi:roborazzi:1.46.1")
    testImplementation("io.github.takahirom.roborazzi:roborazzi-junit-rule:1.46.1")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
}