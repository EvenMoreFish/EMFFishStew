plugins {
    `java-library`
    alias(libs.plugins.shadow)
    alias(libs.plugins.plugin.yml)
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.codemc.io/repository/EvenMoreFish/")
    maven("https://repo.codemc.io/repository/FireML/")
}

dependencies {
    compileOnly(libs.paper.api)
    compileOnly(libs.evenmorefish) {
        exclude("de.tr7zw", "item-nbt-api")
        exclude("com.github.Anon8281", "UniversalScheduler")
    }
    compileOnly(libs.boostedyaml) // Provided by EMF at runtime.

    implementation(libs.daisylib)
    implementation(libs.bstats)
}

group = "org.evenmorefish"
version = properties["project-version"] as String
description = "An EvenMoreFish addon that adds items to start a fishing contest."
java.sourceCompatibility = JavaVersion.VERSION_21

bukkit {
    name = project.name
    version = project.version.toString()
    main = "org.evenmorefish.fishstew.FishStewPlugin"
    apiVersion = "1.21"
    author = "FireML"
    description = project.description.toString()

    depend = listOf("EvenMoreFish")

    permissions {
        register("fishstew.command")
    }
}

tasks {
    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        archiveBaseName.set(project.name)
        archiveVersion.set(project.version.toString())
        archiveClassifier.set("")

        relocate("uk.firedev.daisylib", "org.evenmorefish.fishstew.libs.daisylib")
        relocate("org.bstats", "org.evenmorefish.fishstew.libs.bstats")
    }
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}
