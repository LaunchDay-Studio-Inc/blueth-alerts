plugins {
    java
    id("com.gradleup.shadow") version "9.0.0-beta12"
}

group = property("group") as String
version = property("version") as String

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
}

tasks {
    processResources {
        filteringCharset = "UTF-8"
        filesMatching("plugin.yml") {
            expand("version" to project.version)
        }
    }

    shadowJar {
        archiveClassifier.set("")
        archiveBaseName.set("BluethAlerts")
    }

    build {
        dependsOn(shadowJar)
    }

    jar {
        archiveClassifier.set("unshaded")
    }
}
