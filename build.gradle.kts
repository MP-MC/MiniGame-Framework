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
    compileOnly("org.spigotmc:spigot-api:1.17.1-R0.1-SNAPSHOT")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = "miniGameLib"

            from(components["java"])
        }
    }
}