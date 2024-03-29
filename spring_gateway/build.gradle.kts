import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.9"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	testImplementation("org.springframework.boot:spring-boot-starter-test")

	// aspect
	implementation("org.springframework.boot:spring-boot-starter-aop:2.7.9")

	// H3 Core
	implementation("com.uber:h3:3.7.2")

	// Json
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.5")

	// matrix AEM : sentry
	implementation("io.sentry:sentry-logback:6.5.0")

	// redis
	implementation("org.springframework.boot:spring-boot-starter-data-redis")

	// swagger-doc
	implementation("org.springdoc:springdoc-openapi-ui:1.6.15")
	implementation("org.springdoc:springdoc-openapi-kotlin:1.6.15")

	// mockito v2
	testImplementation("org.mockito:mockito-inline:2.13.0")

}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
