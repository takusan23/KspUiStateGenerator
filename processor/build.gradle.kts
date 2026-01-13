plugins {
    kotlin("jvm")
}

group = "io.github.takusan23.kspuistategenerator.processor"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation(project(":annotation")) // アノテーションを取り込む
    implementation("com.google.devtools.ksp:symbol-processing-api:2.3.4") // ksp も取り込む
}

tasks.test {
    useJUnitPlatform()
}