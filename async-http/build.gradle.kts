import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktorVersion : String by project
val logbackVersion : String by project
val jacksonVersion : String by project

plugins {
    kotlin("jvm")
}

dependencies {
    compile(kotlin("stdlib"))
    compile("io.ktor:ktor-server-netty:$ktorVersion")
    compile("io.ktor:ktor-jackson:$ktorVersion")
    compile("io.ktor:ktor-client-apache:$ktorVersion")
    compile("io.ktor:ktor-client-json:$ktorVersion")
    compile("io.ktor:ktor-client-jackson:$ktorVersion")
    compile("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    compile("ch.qos.logback:logback-classic:$logbackVersion")
}