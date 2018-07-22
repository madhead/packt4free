package by.dev.madhead.packt4free

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.ScheduledEvent
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder
import com.amazonaws.services.simpleemail.model.*
import org.apache.logging.log4j.LogManager

class Handler : RequestHandler<ScheduledEvent, Unit> {
	companion object {
		private val logger = LogManager.getLogger(Handler::class.java)
	}

	override fun handleRequest(input: ScheduledEvent, context: Context) {
		logger.info("test")

		val simpleEmailService = AmazonSimpleEmailServiceClientBuilder
			.defaultClient()

		simpleEmailService.sendEmail(
			SendEmailRequest()
				.withDestination(Destination().withToAddresses(System.getenv("EMAIL_TO")!!))
				.withSource(System.getenv("EMAIL_FROM")!!)
				.withMessage(Message()
					.withSubject(Content().withCharset("UTF-8").withData("TEST"))
					.withBody(Body().withText(Content().withCharset("UTF-8").withData("TEST")))
				)
		)

		logger.info("email sent")
	}
}
