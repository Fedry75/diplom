import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm {
        jvmToolchain(11)
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation("org.jetbrains.exposed:exposed-core:0.40.1")
                implementation("org.jetbrains.exposed:exposed-dao:0.40.1")
                implementation("org.jetbrains.exposed:exposed-jdbc:0.40.1")
                implementation("org.xerial:sqlite-jdbc:3.39.3.0")
                implementation("org.jetbrains.exposed:exposed-java-time:0.40.1")
                implementation("org.slf4j:slf4j-simple:2.0.3")

            }

        }
        val jvmTest by getting

    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "demo"
            packageVersion = "1.0.0"
        }
    }
}
