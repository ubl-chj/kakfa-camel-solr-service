package staatsbibliothek.berlin.hsp.indexupdateservice;

import static java.io.File.separator;
import static staatsbibliothek.berlin.hsp.indexupdateservice.JsonSerializer.serialize;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.wnameless.json.flattener.JsonFlattener;

import de.staatsbibliothek.berlin.hsp.domainmodel.entities.Dokument;
import de.staatsbibliothek.berlin.hsp.domainmodel.messaging.ActivityStreamMessage;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class SolrUpdateProcessor implements Processor {
  private final JdkHttpClient client = new JdkHttpClientImpl();
  private static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());
  SolrUpdateProcessor() {
  }

  public void process(Exchange exchange) throws Exception {
    final String uriString = buildRequestURI(exchange);
    final String jsonActivityStream = exchange.getIn().getBody(String.class);

    // map body to ActivityStream, extract content, reserialize as SolrDocument
    final ActivityStreamMessage stream = MAPPER.readValue(jsonActivityStream, new TypeReference<ActivityStreamMessage>() {
    });
    final Dokument content = stream.getObject().getContent();
    final Optional<String> kodString = serialize(content);
    final byte[] flatJson = JsonFlattener.flatten(kodString.orElse("")).getBytes(
        StandardCharsets.UTF_8);
    final InputStream jsonInput = new ByteArrayInputStream(flatJson);
    client.post(uriString, jsonInput, "application/json");
  }

  private String buildRequestURI(Exchange exchange) throws Exception {
    final String collection = exchange.getContext().resolvePropertyPlaceholders("{{solr.core}}");
    final String solrHost = exchange.getContext().resolvePropertyPlaceholders("{{solr.host}}");
    final String solrPort = exchange.getContext().resolvePropertyPlaceholders("{{solr.port}}");
    final String solrScheme = exchange.getContext().resolvePropertyPlaceholders("{{solr.scheme}}");
    final String solrContext = exchange.getContext().resolvePropertyPlaceholders("{{solr.context}}");
    return solrScheme + ":" + separator + separator + solrHost + ":"
        + solrPort + separator + solrContext + separator + collection + "/update/json/docs?commit=true";
  }
}
