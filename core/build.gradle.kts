plugins {
    kotlin("jvm")
}

val junitVersion : String by project

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("script-runtime"))
    implementation(kotlin("compiler-embeddable"))
    implementation(kotlin("script-util"))
    api("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    implementation("org.junit.platform:junit-platform-launcher:1.2.0")
    implementation("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}
