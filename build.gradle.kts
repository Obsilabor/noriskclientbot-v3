plugins {
    kotlin("jvm") version "1.5.21"
    kotlin("plugin.serialization") version "1.5.21"
    application
}

group = "me.obsilabor"
version = "3.0.0"

repositories {
    mavenCentral()
    jcenter() /* BlueUtils & kordx.emoji */
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    //Kord
    implementation("dev.kord:kord-core:0.8.0-M5")
    implementation("com.gitlab.kordlib:kordx.emoji:0.4.0")

    //Config
    implementation("io.github.config4k:config4k:0.4.2")
    implementation("com.google.code.gson:gson:2.8.6")

    //Logging
    implementation("org.slf4j:slf4j-simple:1.7.30")

    //Utils
    implementation("org.apache.commons:commons-lang3:3.11")
    implementation("net.axay:BlueUtils:1.0.2")

    // Kotlin
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
    implementation("io.ktor:ktor-client-core:1.6.1")
    implementation("io.ktor:ktor-client-cio:1.6.1")
    implementation("io.ktor:ktor-client-serialization:1.6.1")
    implementation("io.ktor:ktor-client-gson:1.6.1")
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")

    // MongoDB
    implementation("org.litote.kmongo", "kmongo-core", "4.2.3")
    implementation("org.litote.kmongo", "kmongo-serialization-mapping", "4.2.3")
}

application {
    mainClass.set("me.obsilabor.noriskclientbot.ManagerKt")
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>{
        kotlinOptions.jvmTarget = "16"
    }
}

java.sourceCompatibility = JavaVersion.VERSION_16
java.targetCompatibility = JavaVersion.VERSION_16
