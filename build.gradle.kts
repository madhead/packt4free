import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.bmuschko.gradle.docker.tasks.container.DockerCreateContainer
import com.bmuschko.gradle.docker.tasks.container.DockerRemoveContainer
import com.bmuschko.gradle.docker.tasks.container.DockerStartContainer
import com.bmuschko.gradle.docker.tasks.container.DockerWaitContainer
import com.bmuschko.gradle.docker.tasks.image.DockerPullImage
import org.gradle.internal.os.OperatingSystem

buildscript {
	dependencies {
		classpath("de.sebastianboegl.gradle.plugins:shadow-log4j-transformer:2.2.0")
	}
}

plugins {
	kotlin("jvm").version("1.2.51")
	id("com.github.johnrengelman.shadow").version("2.0.4")
	id("com.bmuschko.docker-remote-api").version("3.4.0")
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

tasks {
	val shadowJar by getting(ShadowJar::class) {
		transform(de.sebastianboegl.gradle.plugins.shadow.transformers.Log4j2PluginsFileTransformer::class.java)
	}

	val test by getting(Test::class) {
		useJUnitPlatform()

		testLogging {
			showStandardStreams = true
		}
	}

	val updateProjectLogo by creating

	val pullImage by creating(DockerPullImage::class) {
		repository = "madhead/imagemagick"
		tag = "latest"
	}

	val createContainer by creating(DockerCreateContainer::class) {
		dependsOn(pullImage)
		targetImageId { pullImage.imageId }
		binds = mapOf(
			"$projectDir/src/main/assets" to "/src",
			"$projectDir" to "/build"
		)
		// FIXME: Not sure what to do with other OSes
		if (OperatingSystem.current().isUnix) {
			user = "ug"
				.map {
					Runtime.getRuntime().exec("id -$it").let {
						it.waitFor(5, TimeUnit.SECONDS)
						if (it.exitValue() != 0) {
							throw GradleException("Failed to get current user details")
						}
						it.inputStream.bufferedReader().use { it.readText() }
					}
				}.map {
					it.trim()
				}.joinToString(":")
		}
		setCmd(
			"convert",
			"-background",
			"none",
			"-density",
			"300",
			"-resize",
			"128x128",
			"/src/logo.svg",
			"/build/logo.png"
		)
	}

	val startContainer by creating(DockerStartContainer::class) {
		dependsOn(createContainer)
		targetContainerId { createContainer.containerId }
	}

	val waitContainer by creating(DockerWaitContainer::class) {
		dependsOn(startContainer)
		targetContainerId { createContainer.containerId }
	}

	val removeContainer by creating(DockerRemoveContainer::class) {
		targetContainerId { createContainer.containerId }
	}

	updateProjectLogo.dependsOn(waitContainer)
	updateProjectLogo.finalizedBy(removeContainer)

	val wrapper by creating(Wrapper::class) {
		gradleVersion = "4.8.1"
		distributionType = Wrapper.DistributionType.ALL
	}
}
