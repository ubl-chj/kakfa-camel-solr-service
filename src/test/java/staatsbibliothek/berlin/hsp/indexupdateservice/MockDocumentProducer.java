package staatsbibliothek.berlin.hsp.indexupdateservice;

import de.staatsbibliothek.berlin.hsp.domainmodel.messaging.ActivityStreamMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

public class MockDocumentProducer {
  private static final Logger LOGGER =
      LoggerFactory.getLogger(MockDocumentProducer.class);

  @Value("${kafka.topic}")
  private String kafkaTopic;

  @Autowired
  private KafkaTemplate<String, ActivityStreamMessage> kafkaTemplate;

  public void send(ActivityStreamMessage payload) {
    LOGGER.info("sending payload='{}'", payload);
    kafkaTemplate.send(kafkaTopic, payload);
  }
}
