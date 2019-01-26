plugins {
    kotlin("jvm")
}

val junitVersion : String by project

dependencies {
    api(project(":core"))
    implementation(kotlin("script-runtime"))
    implementation(kotlin("compiler-embeddable"))
    implementation(kotlin("script-util"))
    api("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    implementation("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}