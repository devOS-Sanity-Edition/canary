plugins {
	alias(libs.plugins.loom)
	`maven-publish`
}

group = "one.devos.nautical"
version = providers.environmentVariable("GITHUB_RUN_NUMBER")
    .orElse("99999")
    .map { "2.3.$it" }
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
        maven("https://mvn.devos.one/releases") {
            name = "devOsReleases"
            credentials(PasswordCredentials::class)
        }
    }
}
