plugins {
    `java-library`
//    `maven-publish`
    id("io.izzel.taboolib") version "1.42"
    id("org.jetbrains.kotlin.jvm")
}

taboolib {
    install("common")
    install("common-5")
    install("common-5-shaded")
    install("module-ai")
    install("module-chat")
    install("platform-bukkit")
    install("expansion-command-helper")
    classifier = null
    version = "6.0.9-111"
}
repositories{

        maven {
            url = uri("https://maven.aliyun.com/repository/public/")
        }
        maven {
            url = uri("https://maven.aliyun.com/repository/google/")
        }
    maven("https://repo.tabooproject.org/repository/releases")
    maven("https://jitpack.io")
    mavenCentral()
    maven {
        name = "spigotmc-repo"
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
    maven {
        name = "sonatype"
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
}


dependencies {
    compileOnly("ink.ptms:nms-all:1.0.0")
    compileOnly("ink.ptms.core:v11902:11902-minimize:mapped")
    compileOnly("ink.ptms.core:v11902:11902-minimize:universal")
    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjvm-default=all")
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
//
//publishing {
//    repositories {
//        maven {
//            url = uri("https://repo.tabooproject.org/repository/releases")
//            credentials {
//                username = project.findProperty("taboolibUsername").toString()
//                password = project.findProperty("taboolibPassword").toString()
//            }
//            authentication {
//                create<BasicAuthentication>("basic")
//            }
//        }
//    }
//    publications {
//        create<MavenPublication>("library") {
//            from(components["java"])
//            groupId = project.group.toString()
//        }
//    }
//}
