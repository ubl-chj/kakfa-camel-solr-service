package staatsbibliothek.berlin.hsp.indexUpdateService.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * Created by konrad.eichstaedt@sbb.spk-berlin.de on 22.02.2019.
 */

@Component
public class IndexMessageConsumer implements AcknowledgingMessageListener {

  private static final Logger logger = LoggerFactory.getLogger(IndexMessageConsumer.class);


  @Override
  @KafkaListener(topics = {"hsp-index"}, groupId = "hsp")
  public void onMessage(ConsumerRecord data, Acknowledgment acknowledgment) {

    logger.info("Recieving Record at offset {} with topic {} key {} and data {} ", data.offset(),
        data.topic(), data.key(), data.value());

    logger.debug("Processing something ...");

    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    /**
     if (((KOD) data.value()).getId() == 9) {
     throw new RuntimeException("oops");
     }**/

    acknowledgment.acknowledge();
  }

  @Override
  public void onMessage(Object data) {

  }
}

