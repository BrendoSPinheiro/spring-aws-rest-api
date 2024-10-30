plugins {
    java
    jacoco
    alias(libs.plugins.springBoot)
    alias(libs.plugins.dependencyManagement)
}

group = "br.com.brendosp"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.springActuator)
    implementation(libs.springSecurity)
    implementation(libs.springValidation)
    implementation(libs.springWeb)
    implementation(libs.springDoc)
    implementation(libs.jJwtApi)
    implementation(libs.dynamoDB)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    runtimeOnly(libs.bundles.jjwtRuntime)

    developmentOnly(libs.springDevtools)

    testImplementation(libs.bundles.tests)
    testRuntimeOnly(libs.junitPlatformLauncher)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.jacocoTestReport {
    dependsOn("test")
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}
