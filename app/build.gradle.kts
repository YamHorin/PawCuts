plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.services)
    alias(libs.plugins.map.secret)

}

android {
    namespace = "com.example.pawcuts"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.pawcuts"
        minSdk = 26
        targetSdk = 33
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
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.googleid)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation (libs.firebase.ui.auth)
    implementation(libs.play.services.auth)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.database)


    //Google Account
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation (libs.credentials.vlatestversion)
    implementation (libs.androidx.credentials.play.services.auth.vlatestversion)
    implementation (libs.googleid.vlatestversion)

    //Find Location From Text
    androidTestImplementation(libs.espresso.core)

    //Glide
    implementation (libs.glide)

    //gson
    implementation (libs.gson)

    //Google map
    implementation(libs.play.services.location.v2101)
    implementation(libs.play.services.location)
    implementation(libs.google.maps)


}