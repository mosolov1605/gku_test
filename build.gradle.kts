plugins {
    // Apply the java plugin to add support for Java
    java

    // Apply the application plugin to add support for building an application
    application

    // Apply the groovy plugin to also add support for Groovy (needed for Spock)
    groovy

    //Idea
    idea

    id("org.springframework.boot") version "2.1.7.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation("com.google.guava:guava:27.0.1-jre")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-starter-jdbc:2.1.7.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-security:2.1.7.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    /*implementation("org.springframework.security.oauth:spring-security-oauth2:2.3.6.RELEASE")*/
    /*implementation("org.springframework.security:spring-security-jwt:1.0.10.RELEASE")*/
    implementation("com.h2database:h2")
    implementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.springframework.security:spring-security-test")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    runtime("org.springframework.boot:spring-boot-starter-tomcat")

    testImplementation("org.codehaus.groovy:groovy-all:2.5.6")
    testImplementation("org.spockframework:spock-core:1.2-groovy-2.5")

    compileOnly("org.projectlombok:lombok:1.18.8")
    annotationProcessor("org.projectlombok:lombok:1.18.8")
}

application {
    mainClassName = "ru.mosolov.gku.GkuTest"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}



