plugins {
    id("com.github.johnrengelman.shadow")
    kotlin("jvm") version "1.7.20"
}

group = "com.yzqdev.mcplug.tutor.demo"
version = "1.0.0"


dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.7.20")
    implementation("org.jetbrains:annotations:23.0.0")
    compileOnly("org.spigotmc:spigot-api:1.19.2-R0.1-SNAPSHOT")
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


//processResources {
//    val props = mapOf("version" to version)
//    inputs.properties props
//    filteringCharset= "UTF-8"
//    filesMatching("plugin.yml") {
//        expand= props
//    }
//}
