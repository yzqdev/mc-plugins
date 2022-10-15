plugins {
//    java
//    `java-library`
    kotlin("jvm") version "1.7.20"
    id("com.github.johnrengelman.shadow")
}

group = "org.lores"
version = "1.0.0"


dependencies {
    // https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-gradle-plugin
//    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.20")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.7.20")
    implementation("org.jetbrains:annotations:23.0.0")
    compileOnly("me.clip:placeholderapi:2.10.10")
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
