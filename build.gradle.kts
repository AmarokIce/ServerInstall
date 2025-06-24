plugins {
    kotlin("jvm") version "1.7.10"
}

group = "club.someoneice.server_install"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("club.snowlyicewolf:amarok-json-for-java:1.7.8")
}

tasks.jar.configure {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    manifest.attributes["Main-Class"] = "club.someoneice.server_install.MainKt"
    from(configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) })
}