plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp") version "2.3.3"
}

group = "io.github.takusan23.kspuistategenerator.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    // アノテーションと ksp を入れる
    implementation(project(":annotation"))
    ksp(project(":processor"))
}

tasks.test {
    useJUnitPlatform()
}