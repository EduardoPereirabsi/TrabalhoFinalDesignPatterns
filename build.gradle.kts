plugins {
    `java-library`
    `maven-publish`
    id("com.diffplug.spotless") version "8.9.0"
}

group = "br.pucpr"
version = "1.0.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
    withSourcesJar()
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

spotless {
    java {
        googleJavaFormat()
    }
}

tasks.register<JavaExec>("exemplo") {
    group = "documentation"
    description = "Executa o exemplo de uso da biblioteca (nao faz parte do jar publicado)"
    classpath = sourceSets["test"].runtimeClasspath
    mainClass.set("br.pucpr.pulse.exemplo.ExemploDeUso")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            pom {
                name.set("Pulse")
                description.set("Biblioteca de logging e telemetria in-process extensivel")
            }
        }
    }
}
