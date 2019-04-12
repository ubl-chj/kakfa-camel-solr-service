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

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
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
        String core = solrProperties.getCore();
        String urlString = scheme + ":" + separator + separator + host + ":"
                + port + separator + context + separator + core;
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
                .json(JsonLibrary.Jackson)
                .process(new ActivityStreamProcessor())
                //.to("file://{{serialization.log}}");
                .to("direct:docIndex");
        from("direct:docIndex").routeId("DocIndex")
                .process(exchange -> {
                // TODO
                });
    }
}

