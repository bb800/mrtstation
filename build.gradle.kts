plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.30"
    id("org.jetbrains.kotlin.kapt") version "1.4.30"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.4.30"
    id("com.github.johnrengelman.shadow") version "6.1.0"
    id("io.micronaut.application") version "1.4.2"
}

version = "0.1"
group = "assignments"

val kotlinVersion=project.properties.get("kotlinVersion")
repositories {
    mavenCentral()
}

micronaut {
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("assignments.*")
    }
}

dependencies {
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
    implementation("io.micronaut:micronaut-validation")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    runtimeOnly("ch.qos.logback:logback-classic")
    testImplementation("io.mockk:mockk")
}


application {
    mainClass.set("assignments.ApplicationKt")
}
java {
    sourceCompatibility = JavaVersion.toVersion("1.8")
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}
