plugins {
    kotlin("jvm")
}

group = "io.github.takusan23.kspuistategenerator.annotation"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}