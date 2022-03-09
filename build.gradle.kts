plugins {
    `maven-publish`
    kotlin("multiplatform") version "1.5.31"
    java
}

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        testRuns["test"].executionTask.configure {
            useJUnit()
        }
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation("io.netty:netty-all:4.1.72.Final")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
