package staatsbibliothek.berlin.hsp.indexupdateservice;

import static junit.framework.TestCase.assertEquals;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.MockEndpoints;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.MapSolrParams;
import org.junit.Before;
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
public class IndexUpdateServiceApplicationTest {
  private static final String SENDER_TOPIC = "hsp.t";

  @ClassRule
  public static EmbeddedKafkaRule embeddedKafka = new EmbeddedKafkaRule(
      1, true, SENDER_TOPIC);
  @Autowired
  CamelContext camelContext;
  @Autowired
  RouteBuilder kafkaCamelConsumerComponent;
  private ActivityStream stream;
  @EndpointInject(uri = "mock:direct:serialize")
  private MockEndpoint mockSerialize;
  @EndpointInject(uri = "mock:direct:delete.solr")
  private MockEndpoint mockDelete;
  @EndpointInject(uri = "mock:direct:update.solr")
  private MockEndpoint mockUpdate;
  @Autowired
  private MockDocumentProducer producer;
  @Autowired
  private HttpSolrClient solrClient;

  @Before
  public void setUp() {
    this.stream = new RandomMessage().buildRandomActivityStreamMessage();
  }

  @Test
  public void testDocumentIsSerialized() {
    try {
      producer.send(stream);
      camelContext.start();
      mockSerialize.expectedFileExists("/tmp/output.log/" + stream.getObject().getId());
      mockSerialize.assertIsSatisfied();
      camelContext.stop();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testDocumentIsDeleted() {
    final CountDownLatch latch = new CountDownLatch(3);
    mockDelete.whenAnyExchangeReceived(exchange -> latch.countDown());
    try {
      camelContext.start();
      producer.send(stream);
      latch.await(1, TimeUnit.SECONDS);
      stream.setType("Delete");
      producer.send(stream);
      latch.await(1, TimeUnit.SECONDS);
      final String docId = stream.getObject().getId();
      final QueryResponse response = solrClient.query("hsp", buildQueryParams(docId));
      final SolrDocumentList documents = response.getResults();
      assertEquals(0, documents.getNumFound());
      mockDelete.expectedMessageCount(1);
      mockDelete.assertIsSatisfied();
      camelContext.stop();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private MapSolrParams buildQueryParams(String id) {
    final Map<String, String> queryParamMap = new HashMap<>();
    final String query = "id:" + id;
    queryParamMap.put("q", query);
    return new MapSolrParams(queryParamMap);
  }
}
