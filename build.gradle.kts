plugins {
    id("java")
}

group = "uk.qumarth"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

    maven { url = uri("https://jitpack.io") }

    // We need the jogamp maven directoy has it has the latest JARs
    // This is a dependency of processing
    maven { url = uri("https://jogamp.org/deployment/maven") }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // Processing
    implementation("com.github.micycle1:processing-core-4:4.3.3")
}

tasks.test {
    useJUnitPlatform()
}