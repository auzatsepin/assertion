import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    base
    kotlin("jvm") version "1.3.11" apply false
}

allprojects {

    group = "com.github"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
        jcenter()
        maven("https://kotlin.bintray.com/kotlinx")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

}

dependencies {
    subprojects.forEach {
        archives(it)
    }
}

tasks.withType<Wrapper> {
    gradleVersion = "5.1.1"
}
