plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }


    //TODO: REMOVER AQUI
    dependencies {
        //Pager
        implementation("androidx.paging:paging-common:3.3.1")
        //Tests
        testImplementation("io.mockk:mockk:1.13.10")
        testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
        testImplementation("app.cash.turbine:turbine:1.0.0")
        testImplementation("androidx.paging:paging-common:3.1.1")
    }
}
dependencies {
    //Modules
    implementation(project(":core:common"))
    //Tests
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    testImplementation(libs.androidx.paging.common)
    testImplementation(libs.junit)
}