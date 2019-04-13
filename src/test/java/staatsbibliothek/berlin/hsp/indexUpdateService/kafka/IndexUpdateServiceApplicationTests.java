package staatsbibliothek.berlin.hsp.indexUpdateService.kafka;

import static org.apache.camel.Exchange.FILE_NAME;
import static org.apache.camel.Exchange.HTTP_RESPONSE_CODE;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

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

    @EndpointInject(uri = "mock:direct:serialize")
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
            final ActivityStream stream = new ActivityStream();
            stream.setSummary("This is a target document");
            final Target target = new Target();
            final UUID uuid = UUID.randomUUID();
            target.setId(uuid.toString());
            stream.setTarget(target);
            LocalDateTime localDate = LocalDateTime.now();
            stream.setPublished(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(localDate));
            producer.send(stream);
            mock.expectedFileExists("/tmp/output.log/" + uuid.toString());
            mock.assertIsSatisfied();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
