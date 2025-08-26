package org.sportygroup.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sportygroup.model.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.CopyOnWriteArraySet;

@Service
public class EventService {
    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    private final CopyOnWriteArraySet<String> liveEvents = new CopyOnWriteArraySet<>();

    @Autowired
    private ExternalApiService externalApiService;
    @Autowired
    private KafkaProducerService kafkaProducerService;

    public void updateEventStatus(EventStatus eventStatus) {
        String eventId = eventStatus.getEventId();
        boolean isLive = eventStatus.isLive();

        if (isLive) {
            liveEvents.add(eventId);
            logger.info("Event {} is now LIVE", eventId);
        } else {
            liveEvents.remove(eventId);
            logger.info("Event {} is no longer LIVE", eventId);
        }
    }

    @Scheduled(fixedRate = 10000) // Every 10 seconds
    public void pollLiveEvents() {
        logger.info("pollLiveEvents Task");
        if (liveEvents.isEmpty()) {
            logger.debug("No live events to poll");
            return;
        }

        logger.info("Polling {} live events", liveEvents.size());

        for (String eventId : liveEvents) {
            try {
                externalApiService.fetchEventData(eventId)
                        .ifPresentOrElse(
                                response -> {
                                    kafkaProducerService.sendMessage(response);
                                    logger.info("Successfully processed event {}", eventId);
                                },
                                () -> logger.error("Failed to fetch data for event {}", eventId)
                        );
            } catch (Exception e) {
                logger.error("Error processing event {}: {}", eventId, e.getMessage());
            }
        }
    }

    public boolean isEventLive(String eventId) {
        return liveEvents.contains(eventId);
    }
}
