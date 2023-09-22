package com.catchpoint.tracing.demo.todo.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;

/**
 * @author sozal
 */
public class NotificationMessageHandler implements RequestHandler<SQSEvent, Void> {

    @Override
    public Void handleRequest(SQSEvent sqsEvent, Context context) {
        context.getLogger().log("_X_AMZN_TRACE_ID: " + System.getenv("_X_AMZN_TRACE_ID"));
        for (SQSEvent.SQSMessage message : sqsEvent.getRecords()) {
            context.getLogger().log("Handling notification message: " + message.getBody());
        }

        return null;
    }

}
