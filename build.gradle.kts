plugins {
	alias(libs.plugins.loom)
	`maven-publish`
}

group = "one.devos.nautical"
version = providers.environmentVariable("GITHUB_RUN_NUMBER")
    .orElse("99999")
    .map { "3.0.$it" }
    .get()

dependencies {
	minecraft(libs.minecraft)
    api(libs.bundles.fabric)
}

tasks.processResources {
    val properties = mapOf(
        "version" to version,
        "minecraft_version" to libs.versions.minecraft.get(),
        "loader_version" to libs.versions.fabric.loader.get(),
        "fabric_api_version" to libs.versions.fabric.api.get()
    )

    inputs.properties(properties)

    filesMatching("fabric.mod.json") {
        expand(properties)
    }
}

tasks.test {
    // we don't have unit tests, we have a test mod
    failOnNoDiscoveredTests = false
}

loom.runs {
    register("testClient") {
        client()
        name("Test Client")
        source(sourceSets.test.get())
    }

    register("testServer") {
        server()
        name("Test Server")
        source(sourceSets.test.get())
    }

    configureEach {
        vmArg("-Dmixin.debug.export=true")
    }
}

java {
	withSourcesJar()
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }

    repositories {
        listOf("Releases", "Snapshots").forEach {
            maven("https://mvn.devos.one/${it.lowercase()}") {
                name = "devOs$it"
                credentials(PasswordCredentials::class)
            }
        }
    }
}
