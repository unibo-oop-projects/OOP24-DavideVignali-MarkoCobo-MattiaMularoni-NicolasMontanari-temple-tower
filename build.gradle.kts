plugins {
    java
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("org.danilopianini.gradle-java-qa") version "1.82.0"
}

repositories {
    mavenCentral()
}

val javaFXModules = listOf(
    "base",
    "controls",
    "media",
    "fxml",
    "swing",
    "graphics"
)

// Detect OS architecture
val osName = System.getProperty("os.name").toLowerCase()
val arch = System.getProperty("os.arch")
val platform = when {
    osName.contains("mac") && arch.contains("aarch64") -> "mac-aarch64"
    osName.contains("mac") -> "mac"
    osName.contains("win") -> "win"
    osName.contains("linux") -> "linux"
    else -> throw GradleException("Piattaforma non supportata: $osName $arch")
}

dependencies {
    val javaFxVersion = "23.0.1"

    // JavaFX modules
    for (module in javaFXModules) {
        implementation("org.openjfx:javafx-$module:$javaFxVersion:$platform")
    }

    // Suppressions for SpotBugs
    compileOnly("com.github.spotbugs:spotbugs-annotations:4.8.6")

    // JUnit API and testing engine
    val jUnitVersion = "5.11.4"
    testImplementation("org.junit.jupiter:junit-jupiter-api:$jUnitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jUnitVersion")

    // Gson library
    implementation("com.google.code.gson:gson:2.10.1")

    //logger
    implementation ("org.apache.logging.log4j:log4j-core:2.16.0")
}

tasks.withType<Test> {
    // Enables JUnit 5 Jupiter module
    useJUnitPlatform()
}

val main: String by project

application {
    // Define the main class for the application
    mainClass.set(main)
}
