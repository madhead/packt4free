package by.dev.madhead.packt4free

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.ScheduledEvent
import org.apache.logging.log4j.LogManager

class Handler : RequestHandler<ScheduledEvent, Unit> {
	companion object {
		private val logger = LogManager.getLogger(Handler::class.java)
	}

	override fun handleRequest(input: ScheduledEvent, context: Context) {
		logger.info("Got request: ${input}")
	}
}
