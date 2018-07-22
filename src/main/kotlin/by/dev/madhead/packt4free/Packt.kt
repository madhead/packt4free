package by.dev.madhead.packt4free

import org.apache.logging.log4j.LogManager
import org.jsoup.Jsoup
import java.net.URL

class Packt {
	companion object {
		private val logger = LogManager.getLogger(Packt::class.java)
		private val link = "https://www.packtpub.com/packt/offers/free-learning"
	}

	fun dealOfTheDay(): DealOfTheDay {
		val page = Jsoup.parse(
			URL(link),
			try {
				System.getenv("PAGE_PARSE_TIMEOUT").toInt()
			} catch (_: Exception) {
				logger.warn("Cannot parse PAGE_PARSE_TIMEOUT environment variable, falling back to default")
				3000
			}
		)

		return DealOfTheDay(
			title = page.selectFirst("#deal-of-the-day .dotd-title h2")!!.text()!!.trim(),
			coverImage = page.selectFirst("#deal-of-the-day .dotd-main-book-image img")!!.attr("src")!!.trim(),
			link = link
		)
	}
}
