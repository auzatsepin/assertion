import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

val junitVersion : String by project

dependencies {
    compile(kotlin("stdlib"))
    compile(kotlin("reflect"))
    testCompile("org.junit.jupiter:junit-jupiter-api:$junitVersion")
}