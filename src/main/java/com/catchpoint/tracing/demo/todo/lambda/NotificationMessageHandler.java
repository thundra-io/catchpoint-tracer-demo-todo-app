package com.catchpoint.tracing.demo.todo.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import io.opentelemetry.instrumentation.awslambdacore.v1_0.TracingRequestHandler;
import io.opentelemetry.sdk.autoconfigure.AutoConfiguredOpenTelemetrySdk;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sozal
 */
public class NotificationMessageHandler extends TracingRequestHandler<SQSEvent, Void> {

    private static final String TRACE_PARENT = "traceparent";
    private static final String TRACE_STATE = "tracestate";
    private static final List<String> HEADER_NAMES =
            Collections.unmodifiableList(Arrays.asList(TRACE_PARENT, TRACE_STATE));

    public NotificationMessageHandler() {
        super(AutoConfiguredOpenTelemetrySdk.initialize().getOpenTelemetrySdk(), Duration.ofSeconds(3));
    }

    @Override
    protected Void doHandleRequest(SQSEvent event, Context context) {
        for (SQSEvent.SQSMessage message : event.getRecords()) {
            context.getLogger().log("Handling notification message: " + message.getBody());
            context.getLogger().log("Message attributes: " + message.getMessageAttributes());
        }
        return null;
    }

    @Override
    protected Map<String, String> extractHttpHeaders(SQSEvent input) {
        List<SQSEvent.SQSMessage> messages = input.getRecords();
        if (!messages.isEmpty()) {
            SQSEvent.SQSMessage message = messages.get(0);
            Map<String, SQSEvent.MessageAttribute> messageAttributes = message.getMessageAttributes();
            Map<String, String> headers = new HashMap<>();
            if (messageAttributes != null) {
                for (String headerName : HEADER_NAMES) {
                    SQSEvent.MessageAttribute messageAttribute = messageAttributes.get(headerName);
                    if (messageAttribute != null) {
                        String messageAttributeValue = messageAttribute.getStringValue();
                        if (messageAttributeValue != null) {
                            headers.put(headerName, messageAttributeValue);
                        }
                    }
                }
            }
            if (!headers.isEmpty()) {
                return headers;
            }
        }
        return super.extractHttpHeaders(input);
    }

}


