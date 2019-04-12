package staatsbibliothek.berlin.hsp.indexUpdateService.kafka;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThat;
import static org.springframework.kafka.test.assertj.KafkaConditions.key;
import static org.springframework.kafka.test.hamcrest.KafkaMatchers.hasValue;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class IndexUpdateServiceApplicationTests {

	private static final Logger LOGGER =
			LoggerFactory.getLogger(IndexUpdateServiceApplicationTests.class);
	private static String SENDER_TOPIC = "sender.t";

	@Autowired
	private MockDocumentProducer producer;
	private KafkaMessageListenerContainer<String, String> container;
	private BlockingQueue<ConsumerRecord<String, String>> records;

	@ClassRule
	public static EmbeddedKafkaRule embeddedKafka =
			new EmbeddedKafkaRule(1, true, SENDER_TOPIC);

	@Before
	public void setUp() throws Exception {
		Map<String, Object> consumerProperties =
				KafkaTestUtils.consumerProps("sender", "false",
						embeddedKafka.getEmbeddedKafka());

		DefaultKafkaConsumerFactory<String, String> consumerFactory =
				new DefaultKafkaConsumerFactory<>(
						consumerProperties);

		ContainerProperties containerProperties =
				new ContainerProperties(SENDER_TOPIC);

		container = new KafkaMessageListenerContainer<>(consumerFactory,
				containerProperties);

		records = new LinkedBlockingQueue<>();

		container
				.setupMessageListener(new MessageListener<String, String>() {
					@Override
					public void onMessage(
							ConsumerRecord<String, String> record) {
						LOGGER.debug("test-listener received message='{}'",
								record.toString());
						records.add(record);
					}
				});

		container.start();

		ContainerTestUtils.waitForAssignment(container,
				embeddedKafka.getEmbeddedKafka().getPartitionsPerTopic());
	}

	@After
	public void tearDown() {
		container.stop();
	}

	@Test
	public void testSend() throws InterruptedException {
		String greeting = "Hello Spring Kafka Sender!";
		producer.send(greeting);

		ConsumerRecord<String, String> received =
				records.poll(10, TimeUnit.SECONDS);
		assertThat(received, hasValue(greeting));
		assertThat(received).has(key(null));
	}
}
