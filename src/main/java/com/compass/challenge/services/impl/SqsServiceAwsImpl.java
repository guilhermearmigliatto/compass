package com.compass.challenge.services.impl;

import com.compass.challenge.dto.PaymentRequestDTO;
import com.compass.challenge.services.SqsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/*
* This class is an example of AWS SQS Implementation.
* Using SqsServiceSimulatorImpl to simulate SQS runtime execution.
*/

//@Service
public class SqsServiceAwsImpl implements SqsService {

    protected SqsAsyncClient sqsAsyncClient;

    @Value("${aws.sqs.request-queue-url-partial}")
    private String requestQueueUrlPartial;

    @Value("${aws.sqs.response-queue-url-partial}")
    private String responseQueueUrlPartial;

    @Value("${aws.sqs.request-queue-url-total}")
    private String requestQueueUrlTotal;

    @Value("${aws.sqs.response-queue-url-total}")
    private String responseQueueUrlTotal;

    @Value("${aws.sqs.request-queue-url-excess}")
    private String requestQueueUrlExcess;

    @Value("${aws.sqs.response-queue-url-excess}")
    private String responseQueueUrlExcess;

    public SqsServiceAwsImpl(SqsAsyncClient sqsAsyncClient) {
        this.sqsAsyncClient = sqsAsyncClient;
    }

    @Override
    public CompletableFuture<Boolean> processPartialPaymentInQueue(PaymentRequestDTO paymentRequest) {
        return sendAndAwaitResponse(requestQueueUrlPartial, responseQueueUrlPartial, paymentRequest);
    }

    @Override
    public CompletableFuture<Boolean> processTotalPaymentInQueue(PaymentRequestDTO paymentRequest) {
        return sendAndAwaitResponse(requestQueueUrlTotal, responseQueueUrlTotal, paymentRequest);
    }

    @Override
    public CompletableFuture<Boolean> processExcessPaymentInQueue(PaymentRequestDTO paymentRequest) {
        return sendAndAwaitResponse(requestQueueUrlExcess, responseQueueUrlExcess, paymentRequest);
    }

    public CompletableFuture<Boolean> sendAndAwaitResponse(String requestQueueUrl,
                                                           String responseQueueUrl,
                                                           PaymentRequestDTO paymentRequest) {

        String correlationId = java.util.UUID.randomUUID().toString();

        CompletableFuture<Boolean> futureResponse = sendMessageToQueue(requestQueueUrl, paymentRequest, correlationId);

        return futureResponse.thenCompose(sent -> {
            if (sent) {
                return awaitMessageFromResponseQueue(responseQueueUrl, correlationId);
            } else {
                return CompletableFuture.completedFuture(false);
            }
        });
    }

    private CompletableFuture<Boolean> sendMessageToQueue(String requestQueueUrl, Object messageBody, String correlationId) {

        Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
        messageAttributes.put("CorrelationId", MessageAttributeValue.builder()
                .stringValue(correlationId)
                .dataType("String")
                .build());

        SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                .queueUrl(requestQueueUrl)
                .messageBody(messageBody.toString())
                .messageAttributes(messageAttributes)  // Use o mapa que você criou
                .build();

        return sqsAsyncClient.sendMessage(sendMessageRequest)
                .thenApply(response -> {
                    System.out.println("Message sent to request queue, ID: " + response.messageId());
                    return true;
                })
                .exceptionally(throwable -> {
                    System.err.println("Failed to send message to request queue: " + throwable.getMessage());
                    return false;
                });
    }

    private CompletableFuture<Boolean> awaitMessageFromResponseQueue(String responseQueueUrl,
                                                                     String correlationId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                boolean received = false;
                int attempts = 0;
                while (!received && attempts < 10) {
                    ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                            .queueUrl(responseQueueUrl)
                            .waitTimeSeconds(5)
                            .maxNumberOfMessages(1)
                            .build();

                    ReceiveMessageResponse response = sqsAsyncClient.receiveMessage(receiveMessageRequest).join();

                    if (!response.messages().isEmpty()) {
                        Message message = response.messages().get(0);
                        String receivedCorrelationId = message.messageAttributes().get("CorrelationId").stringValue();

                        if (correlationId.equals(receivedCorrelationId)) {
                            System.out.println("Received response from response queue: " + message.body());
                            sqsAsyncClient.deleteMessage(b -> b.queueUrl(responseQueueUrl).receiptHandle(message.receiptHandle())).join();
                            received = true;
                            return true;
                        }
                    }
                    attempts++;
                    TimeUnit.SECONDS.sleep(1);
                }
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
    }

}