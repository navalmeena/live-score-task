import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sportygroup.model.EventStatus;
import org.sportygroup.service.EventService;
import org.sportygroup.service.ExternalApiService;
import org.sportygroup.service.KafkaProducerService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {
//
//    @Mock
//    private ExternalApiService externalApiService;
//
//    @Mock
//    private KafkaProducerService kafkaProducerService;

    private EventService eventService;

    @BeforeEach
    void setUp() {
        eventService = new EventService();
    }

    @Test
    void testUpdateEventStatusToLive() {
        EventStatus eventStatus = new EventStatus();
        eventStatus.setEventId("345");
        eventStatus.setLive(true);
        eventService.updateEventStatus(eventStatus);

        assertTrue(eventService.isEventLive("345"));
    }

    @Test
    void testUpdateEventStatusToNotLive() {
        EventStatus eventStatus = new EventStatus();
        eventStatus.setEventId("345");
        eventStatus.setLive(true);
        eventService.updateEventStatus(eventStatus);

        EventStatus notLiveStatus = new EventStatus();
        eventStatus.setEventId("345");
        eventStatus.setLive(false);
        eventService.updateEventStatus(notLiveStatus);

        assertFalse(eventService.isEventLive("345"));
    }
}