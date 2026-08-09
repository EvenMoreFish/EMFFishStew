rootProject.name = "EMFFishStew"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            // compileOnly dependencies
            library("paper-api", "io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")

            library("evenmorefish", "com.oheers.evenmorefish:even-more-fish-plugin:2.1.5")

            // implementation dependencies
            library("daisylib", "uk.firedev:DaisyLib:4.0-SNAPSHOT")

            // paperLibrary dependencies

            // Gradle plugins
            plugin("shadow", "com.gradleup.shadow").version("9.2.2")
            plugin("plugin-yml", "de.eldoria.plugin-yml.bukkit").version("0.8.0")
        }
    }
}
