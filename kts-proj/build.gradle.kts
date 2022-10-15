plugins {
    id("org.jetbrains.kotlin.jvm") version "1.7.20" apply false

}
extra["compileSdk"]=33
extra["minSdk"]=21
extra["targetSdk"]=33


tasks.register<Delete>("clean").configure {
    delete(rootProject.buildDir)
}
