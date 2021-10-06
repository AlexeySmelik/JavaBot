plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    implementation("org.jsoup:jsoup:1.14.3")
}

application {
    mainClass.set("Main")
}

tasks.test {
    useJUnitPlatform()
}
