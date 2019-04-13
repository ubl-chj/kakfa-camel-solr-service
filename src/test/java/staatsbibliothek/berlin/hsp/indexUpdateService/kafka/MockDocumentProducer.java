package staatsbibliothek.berlin.hsp.indexUpdateService.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

public class MockDocumentProducer {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(MockDocumentProducer.class);

    @Autowired
    private KafkaTemplate<String, ActivityStream> kafkaTemplate;

    public void send(ActivityStream payload) {
        LOGGER.info("sending payload='{}'", payload);
        kafkaTemplate.send("sender.t", payload);
    }
}
