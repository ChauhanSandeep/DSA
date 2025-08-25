plugins {
    java
    application
}

group = "com.tesseract"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    // Testing dependencies
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testImplementation("org.assertj:assertj-core:3.24.2")
    testImplementation("org.mockito:mockito-core:5.7.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.7.0")
    
    // Utility dependencies (optional, for enhanced algorithms)
    implementation("org.apache.commons:commons-lang3:3.14.0")
    implementation("org.apache.commons:commons-collections4:4.4")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

application {
    mainClass.set("Main")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        showStandardStreams = false
    }
}

tasks.compileJava {
    options.encoding = "UTF-8"
}

tasks.compileTestJava {
    options.encoding = "UTF-8"
}

// Custom task to run individual algorithm classes
tasks.register("runAlgorithm", JavaExec::class) {
    group = "application"
    description = "Run a specific algorithm class"
    
    classpath = sourceSets.main.get().runtimeClasspath
    
    // Usage: ./gradlew runAlgorithm -PmainClass=Array.ThreeSum
    if (project.hasProperty("mainClass")) {
        mainClass.set(project.property("mainClass").toString())
    } else {
        mainClass.set("Main")
    }
}

// Task to run all main methods for testing
tasks.register("testAllAlgorithms") {
    group = "verification"
    description = "Test compile all algorithm files"
    dependsOn("compileJava")
    
    doLast {
        println("All algorithms compiled successfully!")
        println("Use 'gradle runAlgorithm -PmainClass=<ClassName>' to run individual algorithms")
    }
}

// Custom source sets configuration to maintain current structure temporarily
sourceSets {
    main {
        java {
            // Add current root-level packages to source path during migration
            srcDirs("src/main/java")
        }
    }
    test {
        java {
            srcDirs("src/test/java")
        }
    }
}
