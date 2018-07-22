package by.dev.madhead.packt4free

import org.apache.logging.log4j.LogManager
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class PacktTest {
	companion object {
		val logger = LogManager.getLogger(PacktTest::class.java)!!
	}

	lateinit var packt: Packt

	@BeforeEach
	fun init() {
		packt = Packt()
	}

	@Test
	@DisplayName("title must not be blank")
	fun `title must not be blank`() {
		val title = Packt().dealOfTheDay().title

		logger.info("DotD title: {}", title)
		Assertions.assertFalse(title.isBlank())
	}

	@Test
	@DisplayName("image src must not be blank")
	fun `image src must not be blank`() {
		val coverImage = Packt().dealOfTheDay().coverImage

		logger.info("DotD cover image: {}", coverImage)
		Assertions.assertFalse(coverImage.isBlank())
	}
}
