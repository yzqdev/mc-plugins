plugins {
    id("org.jetbrains.kotlin.jvm") version "1.7.20" apply false
    id("com.github.johnrengelman.shadow") version "7.1.2" apply false
}
extra["compileSdk"]=33



tasks.register<Delete>("clean").configure {
    delete(rootProject.buildDir)
}
