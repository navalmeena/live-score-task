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
        EventStatus eventStatus = new EventStatus("123", true);

        eventService.updateEventStatus(eventStatus);

        assertTrue(eventService.isEventLive("123"));
    }

    @Test
    void testUpdateEventStatusToNotLive() {
        // First set to live
        EventStatus liveStatus = new EventStatus("123", true);
        eventService.updateEventStatus(liveStatus);

        // Then set to not live
        EventStatus notLiveStatus = new EventStatus("123", false);
        eventService.updateEventStatus(notLiveStatus);

        assertFalse(eventService.isEventLive("123"));
    }
}