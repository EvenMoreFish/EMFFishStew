rootProject.name = "EMFFishStew"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            // compileOnly dependencies
            library("paper-api", "io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
            library("evenmorefish", "com.oheers.evenmorefish:even-more-fish-plugin:2.5.0-SNAPSHOT")

            // implementation dependencies
            library("daisylib", "uk.firedev:DaisyLib:4.2")

            // library dependencies
            library("bstats", "org.bstats:bstats-bukkit:3.2.1")

            // Gradle plugins
            plugin("shadow", "com.gradleup.shadow").version("9.2.2")
            plugin("plugin-yml", "de.eldoria.plugin-yml.bukkit").version("0.8.0")
        }
    }
}
