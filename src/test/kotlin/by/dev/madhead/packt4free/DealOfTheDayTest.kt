package by.dev.madhead.packt4free

import org.apache.logging.log4j.LogManager
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class DealOfTheDayTest {
	companion object {
		val logger = LogManager.getLogger(DealOfTheDayTest::class.java)!!
	}

	lateinit var dealOfTheDay: DealOfTheDay

	@BeforeEach
	fun init() {
		dealOfTheDay = dealOfTheDay()
	}

	@Test
	@DisplayName("title must not be blank")
	fun hello() {
		logger.info("DotD title: {}", dealOfTheDay.title)
		Assertions.assertFalse(dealOfTheDay.title.isBlank())
	}
}
