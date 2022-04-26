plugins {
    `java-library`
    `maven-publish`
}

group = "tk.empee"
version = "1.0"

repositories {
    maven(url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/")

    mavenCentral()
}

dependencies {
    compileOnly("org.jetbrains:annotations:20.1.0")
    compileOnly("org.spigotmc:spigot-api:1.17.1-R0.1-SNAPSHOT")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "gameFramework"

            from(components["java"])
        }
    }
}