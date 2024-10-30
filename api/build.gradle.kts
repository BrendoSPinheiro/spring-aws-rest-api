plugins {
    java
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
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    implementation(libs.springDoc)
    implementation(libs.jJwtApi)
    runtimeOnly(libs.bundles.jjwtRuntime)
    implementation(libs.dynamoDB)

    developmentOnly(libs.springDevtools)

    testImplementation(libs.bundles.tests)
    testRuntimeOnly(libs.junitPlatformLauncher)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
