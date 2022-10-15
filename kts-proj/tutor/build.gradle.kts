import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    kotlin("jvm") version "1.7.20"
    application
}

group = "org.yzqdev.kt"
version = "1.0-SNAPSHOT"


dependencies {
    testImplementation(kotlin("test"))
}
val targetJavaVersion = 17

java {
    val javaVersion = JavaVersion.toVersion(17)
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
    if(JavaVersion.current() < javaVersion) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}