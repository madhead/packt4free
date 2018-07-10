import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

buildscript {
	dependencies {
		classpath("de.sebastianboegl.gradle.plugins:shadow-log4j-transformer:2.2.0")
	}
}

plugins {
	kotlin("jvm").version("1.2.51")
	id("com.github.johnrengelman.shadow").version("2.0.4")
}

repositories {
	jcenter()
}

dependencies {
	implementation(kotlin("stdlib-jdk8", "1.2.51"))

	implementation("com.amazonaws:aws-lambda-java-core:1.2.0")
	implementation("com.amazonaws:aws-lambda-java-events:2.1.0")

	implementation("org.jsoup:jsoup:1.11.3")

	implementation("com.amazonaws:aws-lambda-java-log4j2:1.1.0")
	implementation("org.apache.logging.log4j:log4j-api:2.11.0")
	implementation("org.apache.logging.log4j:log4j-core:2.11.0")

	testImplementation("org.junit.jupiter:junit-jupiter-api:5.2.0")
	testRuntime("org.junit.jupiter:junit-jupiter-engine:5.2.0")
}

val shadowJar by tasks.getting(ShadowJar::class) {
	transform(de.sebastianboegl.gradle.plugins.shadow.transformers.Log4j2PluginsFileTransformer::class.java)
}

val test by tasks.getting(Test::class) {
	useJUnitPlatform()

	testLogging {
		showStandardStreams = true
	}
}

task<Wrapper>("wrapper") {
	gradleVersion = "4.8.1"
	distributionType = Wrapper.DistributionType.ALL
}
