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

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
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
    mainClass.set("br.pucpr.papiro.exemplo.ExemploDeUso")
    jvmArgs("-Dstdout.encoding=UTF-8")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            pom {
                name.set("Papiro")
                description.set("Biblioteca extensivel de geracao e exportacao de documentos")
            }
        }
    }
}
