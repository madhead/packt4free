package by.dev.madhead.packt4free

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.ScheduledEvent
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder
import com.amazonaws.services.simpleemail.model.*
import com.amazonaws.xray.AWSXRay
import org.apache.logging.log4j.LogManager

class Handler : RequestHandler<ScheduledEvent, Unit> {
	companion object {
		private val logger = LogManager.getLogger(Handler::class.java)
	}

	override fun handleRequest(input: ScheduledEvent, context: Context) {
		val dealOfTheDay = AWSXRay.beginSegment("Parse Packt").use {
			Packt().dealOfTheDay()
		}
		val simpleEmailService = AmazonSimpleEmailServiceClientBuilder
			.defaultClient()

		simpleEmailService.sendEmail(
			SendEmailRequest()
				.withDestination(Destination().withToAddresses(System.getenv("EMAIL_TO")!!))
				.withSource(System.getenv("EMAIL_FROM")!!)
				.withMessage(Message()
					.withSubject(Content().withCharset("UTF-8").withData("Packt free eBook @ 07/19/2018: ${dealOfTheDay.title}"))
					.withBody(Body().withHtml(Content().withCharset("UTF-8").withData(
						"""
							<img src="${dealOfTheDay.coverImage}"/>
							<br/>
							<a href="${dealOfTheDay.link}">Claim now!</a>
						""".trimIndent()
					)))
				)
		)
	}
}
