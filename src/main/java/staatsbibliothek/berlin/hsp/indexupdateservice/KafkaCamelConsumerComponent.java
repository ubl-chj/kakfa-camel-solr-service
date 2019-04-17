/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package staatsbibliothek.berlin.hsp.indexupdateservice;

import static java.net.URLEncoder.encode;
import static org.apache.camel.Exchange.FILE_NAME;
import static org.apache.camel.LoggingLevel.INFO;
import static org.trellisldp.camel.ActivityStreamProcessor.ACTIVITY_STREAM_OBJECT_ID;
import static org.trellisldp.camel.ActivityStreamProcessor.ACTIVITY_STREAM_TYPE;

import java.nio.charset.StandardCharsets;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class KafkaCamelConsumerComponent extends RouteBuilder {
  private static final Logger LOGGER = LoggerFactory.getLogger(KafkaCamelConsumerComponent.class);
  private HttpSolrClient solrClient;

  private static final String KAFKA_ROUTE_URI =
      "kafka:{{kafka.topic}}?brokers={{kafka.bootstrap-servers}}"
          + "&maxPollRecords={{kafka.consumer.maxPollRecords}}"
          + "&consumersCount={{kafka.consumer.consumersCount}}"
          + "&seekTo={{kafka.consumer.seekTo}}"
          + "&groupId={{kafka.consumer.group}}";

  @Autowired
  public KafkaCamelConsumerComponent(HttpSolrClient solrClient) {
    this.solrClient = solrClient;
  }

  @Override
  public void configure() {
    LOGGER.info("About to start route: Kafka Server -> Log ");
    CamelContext context = new DefaultCamelContext();

    from(KAFKA_ROUTE_URI)
        .routeId("FromKafka")
        .unmarshal()
        .json(JsonLibrary.Jackson)
        .process(new ActivityStreamProcessor())
        .filter(header(ACTIVITY_STREAM_OBJECT_ID).isNotNull())
        .process(e -> e.getIn().setHeader("SolrSearchId",
            encode(e.getIn().getHeader(ACTIVITY_STREAM_OBJECT_ID, String.class),
                StandardCharsets.UTF_8)))
        .choice()
          .when(header(ACTIVITY_STREAM_TYPE).contains("Delete"))
            .to("direct:delete.solr")
          .otherwise()
            .to("direct:update.solr");
    from("direct:delete.solr").routeId("SolrDelete")
        .log(INFO, LOGGER, "Deleting ${headers.ActivityStreamObjectId} from Solr")
        .process(exchange -> {
          final String collection = getContext().resolvePropertyPlaceholders("{{kafka.consumer.group}}");
          final String id = exchange.getIn().getHeader(ACTIVITY_STREAM_OBJECT_ID, String.class);
          solrClient.deleteById(collection, id);
          solrClient.commit(collection);
        });
    from("direct:update.solr").routeId("SolrUpdate")
        .log(INFO, LOGGER, "Updating ${headers.ActivityStreamObjectId} in Solr")
        .marshal()
        .json(JsonLibrary.Jackson, true)
        .process(new SolrUpdateProcessor())
        .to("direct:serialize");
    from("direct:serialize")
        .process(exchange -> {
          exchange.getOut().setBody(exchange.getIn().getBody(String.class));
          final String filename = exchange.getIn().getHeader(
              ACTIVITY_STREAM_OBJECT_ID, String.class);
          exchange.getOut().setHeader(FILE_NAME, filename);
        })
        .log(INFO, LOGGER, "Filename: ${headers[CamelFileName]}")
        .to("file://{{ius.serialization.log}}");
  }
}

