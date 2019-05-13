package staatsbibliothek.berlin.hsp.indexupdateservice;

import de.staatsbibliothek.berlin.hsp.domainmodel.messaging.ActivityStreamMessage;

import java.util.concurrent.CountDownLatch;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.MockEndpoints;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.test.annotation.DirtiesContext;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest
@DirtiesContext
@MockEndpoints
public class ThresholdTest {
  private static final String SENDER_TOPIC = "hsp.t";
  @ClassRule
  public static EmbeddedKafkaRule embeddedKafka = new EmbeddedKafkaRule(
      1, true, SENDER_TOPIC);
  @Autowired
  CamelContext camelContext;
  @Autowired
  RouteBuilder kafkaCamelConsumerComponent;
  @EndpointInject(uri = "mock:direct:update.solr")
  private MockEndpoint mockUpdate;
  @Autowired
  private MockDocumentProducer producer;

  @Test
  public void testDocumentThreshold() {
    final CountDownLatch latch = new CountDownLatch(3);
    mockUpdate.whenAnyExchangeReceived(exchange -> latch.countDown());
    try {
      camelContext.start();
      final int LOOPS = 20;
      for (int i = 0; i < LOOPS; i++) {
        final ActivityStreamMessage stream = new RandomMessage().buildRandomActivityStreamMessage();
        producer.send(stream);
      }
      mockUpdate.expectedMessageCount(20);
      mockUpdate.assertIsSatisfied();
      camelContext.stop();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
