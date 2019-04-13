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
package staatsbibliothek.berlin.hsp.indexUpdateService.kafka;

import static java.io.File.separator;
import static org.apache.camel.Exchange.FILE_NAME;
import static org.apache.camel.Exchange.HTTP_RESPONSE_CODE;
import static org.apache.camel.LoggingLevel.INFO;

import java.io.InputStream;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.trellisldp.camel.ActivityStreamProcessor;

@Component
public class KafkaCamelConsumerComponent extends RouteBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaCamelConsumerComponent.class);

    private static final String HTTP_ACCEPT = "Accept";
    private HttpSolrClient solrClient;

    @Autowired
    public KafkaCamelConsumerComponent(SolrSearchProperties solrProperties) {
        String host = solrProperties.getHost();
        Integer port = solrProperties.getPort();
        String scheme = solrProperties.getScheme();
        String context = solrProperties.getContext();
        String urlString = scheme + ":" + separator + separator + host + ":"
                + port + separator + context;
        this.solrClient = new HttpSolrClient.Builder(urlString).build();
    }

    @Override
    public void configure() {
        LOGGER.info("About to start route: Kafka Server -> Log ");
        CamelContext context = new DefaultCamelContext();

        from("kafka:{{consumer.topic}}?brokers={{bootstrap-server}}"
                + "&maxPollRecords={{consumer.maxPollRecords}}"
                + "&consumersCount={{consumer.consumersCount}}"
                + "&seekTo={{consumer.seekTo}}"
                + "&groupId={{consumer.group}}")
                .routeId("FromKafka")
                .unmarshal()
                .json(JsonLibrary.Jackson, ActivityStream.class)
                .to("direct:docIndex");
        from("direct:docIndex").routeId("DocIndex")
                .process(exchange -> {
                    final ActivityStream stream = exchange.getIn().getBody(ActivityStream.class);
                    final UpdateResponse response = solrClient.addBean("hsp", stream);
                    solrClient.commit("hsp");
                    exchange.getIn().setHeader(FILE_NAME, stream.getTarget().getId());
                })
                .to("direct:serialize");
        from("direct:serialize")
                .process(exchange -> {
                    exchange.getOut().setBody(exchange.getIn().getBody(ActivityStream.class));
                    final String filename = exchange.getIn().getHeader(FILE_NAME, String.class);
                    exchange.getOut().setHeader(FILE_NAME, filename);
                })
                .marshal()
                .json(JsonLibrary.Jackson, true)
                .log(INFO, LOGGER, "Filename: ${headers[CamelFileName]}")
                .to("file://{{serialization.log}}");
    }
}

