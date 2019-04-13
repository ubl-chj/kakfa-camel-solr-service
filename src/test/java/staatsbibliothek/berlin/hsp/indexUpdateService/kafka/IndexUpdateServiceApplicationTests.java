package staatsbibliothek.berlin.hsp.indexUpdateService.kafka;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.MockEndpoints;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.test.annotation.DirtiesContext;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest
@DirtiesContext
@MockEndpoints
public class IndexUpdateServiceApplicationTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexUpdateServiceApplicationTests.class);
    private static String SENDER_TOPIC = "sender.t";

    @EndpointInject(uri = "mock:direct:docIndex")
    private MockEndpoint mock;

    @Autowired
    private MockDocumentProducer producer;

    @Autowired
    CamelContext camelContext;

    @Autowired
    RouteBuilder kafkaCamelConsumerComponent;

    @ClassRule
    public static EmbeddedKafkaRule embeddedKafka = new EmbeddedKafkaRule(1, true, SENDER_TOPIC);

    @Test
    public void testKafkaRoute() throws Exception {
        camelContext.start();
        try {
            ActivityStream stream = new ActivityStream();
            stream.setContext("https://www.w3.org/ns/activitystreams");
            producer.send(stream);
            mock.await();
            mock.expectedMessageCount(1);
            mock.message(0).body(String.class).contains("https://www.w3.org/ns/activitystreams");
            mock.assertIsSatisfied();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
