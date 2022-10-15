plugins {
    id("com.github.johnrengelman.shadow")
    kotlin("jvm") version "1.7.20"
}

group = "com.yzqdev.mcplug.tutor"
version = "1.0.0"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.7.20")
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
//tasks.withType(JavaCompile).configureEach {
//    if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible()) {
//        options.release = targetJavaVersion
//    }
//}

//processResources {
//    val props = mapOf("version" to version)
//    inputs.properties props
//    filteringCharset= "UTF-8"
//    filesMatching("plugin.yml") {
//        expand= props
//    }
//}
