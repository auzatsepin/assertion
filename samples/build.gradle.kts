plugins {
    kotlin("jvm")
}

val arrowVersion : String by project
val jacksonVersion : String by project
val junitVersion : String by project
val ktorVersion : String by project
val logbackVersion : String by project

dependencies {
    api(project(":core"))
    implementation(kotlin("script-runtime"))
    implementation(kotlin("compiler-embeddable"))
    implementation(kotlin("script-util"))
    api("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    implementation("io.arrow-kt:arrow-core:$arrowVersion")
    implementation("io.arrow-kt:arrow-annotations:$arrowVersion")
    implementation("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    compile("io.ktor:ktor-server-netty:$ktorVersion")
    compile("io.ktor:ktor-jackson:$ktorVersion")
    compile("io.ktor:ktor-client-apache:$ktorVersion")
    compile("io.ktor:ktor-client-json:$ktorVersion")
    compile("io.ktor:ktor-client-jackson:$ktorVersion")
    compile("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    compile("org.slf4j:slf4j-api:1.7.26")
    compile("ch.qos.logback:logback-core:$logbackVersion")
    compile("ch.qos.logback:logback-classic:$logbackVersion")
    compile("io.ktor:ktor-client-logging-jvm:$ktorVersion")
}