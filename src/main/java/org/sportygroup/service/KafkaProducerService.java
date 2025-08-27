package org.sportygroup.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sportygroup.model.ScoreMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.lang.NonNull;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
public class KafkaProducerService {
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);

    @Value("${spring.kafka.topic.name}")
    private String topicName;

    private final KafkaTemplate<String, ScoreMessage> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, ScoreMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Retryable(
            include = {Exception.class},
            maxAttemptsExpression = "${retry.max-attempts:3}",
            backoff = @Backoff(delayExpression = "${retry.delay-ms:1000}")
    )
    public void sendMessage(ScoreMessage message) {
        try {
            ListenableFuture<SendResult<String, ScoreMessage>> future =
                    kafkaTemplate.send(topicName, message.getEventId(), message);

            future.addCallback(new ListenableFutureCallback<SendResult<String, ScoreMessage>>() {
                @Override
                public void onSuccess(SendResult<String, ScoreMessage> result) {
                    logger.info("Sent message successfully to topic {}: {}", topicName, message);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Message metadata: {}", result.getRecordMetadata());
                    }
                }

                @Override
                public void onFailure(@NonNull Throwable ex) {
                    logger.error("Failed to send message to topic {}: {}", topicName, ex.getMessage());
                    // Don't throw exception here, it's already handled by the retry mechanism
                }
            });
        } catch (Exception e) {
            logger.error("Error sending message to Kafka: {}", e.getMessage(), e);
            throw e; // Will be retried
        }
    }
}