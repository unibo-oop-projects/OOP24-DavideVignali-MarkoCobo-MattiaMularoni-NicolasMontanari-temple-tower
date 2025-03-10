
plugins {
    java
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("org.danilopianini.gradle-java-qa") version "1.82.0"

    id("org.danilopianini.unibo-oop-gradle-plugin") version "1.1.7"
}

java { toolchain { languageVersion.set(JavaLanguageVersion.of(21)) } }


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

dependencies {
    val javaFxVersion = "17.0.2"

    listOf("win", "mac", "linux", "mac-aarch64").forEach { targetPlatform ->
        javaFXModules.forEach { module ->
            implementation("org.openjfx:javafx-$module:$javaFxVersion:$targetPlatform")
        }
    }

    // Suppressions for SpotBugs
    compileOnly("com.github.spotbugs:spotbugs-annotations:4.8.6")

    // JUnit API and testing engine
    val jUnitVersion = "5.11.4"
    testImplementation("org.junit.jupiter:junit-jupiter-api:$jUnitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jUnitVersion")

    // Gson library
    implementation("com.google.code.gson:gson:2.10.1")

    // SLF4J and Logback
    implementation("org.slf4j:slf4j-api:1.7.32")
    implementation("ch.qos.logback:logback-classic:1.2.6")

    // Apache Commons IO
    implementation("commons-io:commons-io:2.11.0")

    testImplementation("org.testfx:testfx-core:4.0.18")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.1")
    testImplementation("org.testfx:testfx-junit5:4.0.18")
    testImplementation("org.mockito:mockito-core:5.4.0")
    testImplementation("org.hamcrest:hamcrest:2.2")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21)) 
    }
}

tasks.withType<Test> {
    // Enables JUnit 5 Jupiter module
    useJUnitPlatform()
}

tasks.shadowJar {
    manifest {
        attributes(
            "Main-Class" to "it.unibo.templetower.App",
            "Multi-Release" to "true"
        )
    }
    mergeServiceFiles()
    archiveClassifier.set("")
    
    from("src/main/resources") {
        include("*/.css")
        include("*/.png")
        include("*/.jpg")
        include("*/.gif")
        include("*/.wav")
        include("*/.mp4")
    }

}

tasks.distZip {
    dependsOn(tasks.shadowJar)
}

tasks.distTar {
    dependsOn(tasks.shadowJar)
}

tasks.startScripts {
    dependsOn(tasks.shadowJar)
}

tasks.startShadowScripts {
    dependsOn(tasks.jar)
}
val main: String by project

application {
    // Define the main class for the application
    mainClass.set(main)
}