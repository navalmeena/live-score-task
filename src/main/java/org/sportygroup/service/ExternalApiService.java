package org.sportygroup.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sportygroup.model.ExternalApiResponse;
import org.sportygroup.model.ScoreMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class ExternalApiService {
    private static final Logger logger = LoggerFactory.getLogger(ExternalApiService.class);

    @Value("${external.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    public ExternalApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Correct retry configuration - using include instead of retryFor
    @Retryable(
            include = {RestClientException.class}, // Use include instead of retryFor
            maxAttemptsExpression = "${retry.max-attempts:3}",
            backoff = @Backoff(delayExpression = "${retry.delay-ms:1000}")
    )
    public Optional<ScoreMessage> fetchEventData(String eventId) {
        try {
            String url = apiUrl + "/" + eventId;
            logger.debug("Calling external API: {}", url);

            ResponseEntity<ExternalApiResponse> response =
                    restTemplate.getForEntity(url, ExternalApiResponse.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                ExternalApiResponse apiResponse = response.getBody();
                ScoreMessage message = new ScoreMessage();
                message.setEventId(apiResponse.getEventId());
                message.setScore(apiResponse.getCurrentScore());

                return Optional.of(message);
            } else {
                logger.warn("External API returned non-success status: {}", response.getStatusCode());
                return Optional.empty();
            }
        } catch (RestClientException e) {
            logger.error("Failed to call external API for event {}: {}", eventId, e.getMessage());
            throw e; // Will be retried
        } catch (Exception e) {
            logger.error("Unexpected error calling API for event {}: {}", eventId, e.getMessage());
            return Optional.empty(); // Won't be retried
        }
    }
}
