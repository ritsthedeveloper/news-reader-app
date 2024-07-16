import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.googleHiltAndroid)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.ritesh.newsreader"
    compileSdk = 34

    signingConfigs {
        /** This release config contains key required to sign the apk. When we rollout app in apk format,
        we will need to sign it using this key. */
        create("release_config") {}
        /** This app bundle release config contains key required to sign the app bundle. When we rollout app in app
         * bundle format, we will need to sign it using this key while uploading to play store, then play store will use
         * release_config key to sign the apk generated from app bundle. */
        create("app_bundle_release_config") {}

        create("debug_config") {}
    }

    defaultConfig {
        applicationId = "com.ritesh.newsreader"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        //load the values from .properties file
        val keystoreFile = project.rootProject.file("apikey.properties")
        val properties = Properties()
        properties.load(keystoreFile.inputStream())

        //return empty key in case something goes wrong
        val apiKey = properties.getProperty("NEWS_API_KEY") ?: ""

        buildConfigField(
            type = "String",
            name = "API_KEY",
            value = apiKey
        )
    }

    buildTypes {
        debug {
            /**TODO: remove the string and add your api key( Not required anymore as it has been now added to
             * .properties file */
//            buildConfigField("String", "API_KEY", "\"<Add your API Key>\"")
        }
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
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    /* Configuration for Android App bundle -
    the bundle block is used to control which types of configuration APKs
    you want your app bundle to support.*/
    bundle {
        language {
            // Specifies that the app bundle should not support
            // configuration APKs for language resources. These
            // resources are instead packaged with each base and
            // dynamic feature APK.
            enableSplit = false
        }
        density {
            // This property is set to true by default.
            // Different APKs are generated for devices with different screen densities.
            enableSplit = true
        }
        abi {
            // This property is set to true by default.
            // Different APKs are generated for devices with different CPU architectures.
            enableSplit = true
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.google.android.material)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
    implementation(libs.gson)
    // Moshi Dependencies
//    implementation(libs.moshi.converter)
//    implementation(libs.moshi.codegen)

    //Dagger-Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    kapt(libs.hilt.android.compiler)
    implementation(libs.hilt.common)

    //RoomDB
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    annotationProcessor(libs.room.compiler)
    kapt(libs.room.compiler)
//    ksp(libs.room.compiler)
//

    //Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    //Compose
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    // Coil - Image loading lib
    implementation(libs.coil.compose)

    // Paging library
    implementation(libs.androidx.paging.runtime)
    // Paging - Jetpack Compose integration
    implementation(libs.androidx.paging.compose)

    // Timber- Logger
    implementation(libs.logger.timber)

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    // Mockito Test
    testImplementation(libs.mockito.core)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.arch.core.testing)
    testImplementation(libs.app.cash.turbine)

    //Not a processor, but forces Dagger to use newer metadata lib
//    kapt(libs.kotlinx.metadata.jvm)
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}