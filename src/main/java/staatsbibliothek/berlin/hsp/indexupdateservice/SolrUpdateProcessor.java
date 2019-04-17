package staatsbibliothek.berlin.hsp.indexupdateservice;

import static java.io.File.separator;

import com.github.wnameless.json.flattener.JsonFlattener;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class SolrUpdateProcessor implements Processor {
  private final JdkHttpClient client = new JdkHttpClientImpl();

  public SolrUpdateProcessor() {
  }

  public void process(Exchange exchange) throws Exception {
    final String collection = exchange.getContext().resolvePropertyPlaceholders("{{kafka.consumer.group}}");
    final String solrHost = exchange.getContext().resolvePropertyPlaceholders("{{solr.host}}");
    final String solrPort = exchange.getContext().resolvePropertyPlaceholders("{{solr.port}}");
    final String solrScheme = exchange.getContext().resolvePropertyPlaceholders("{{solr.scheme}}");
    final String solrContext = exchange.getContext().resolvePropertyPlaceholders("{{solr.context}}");
    final String jsonActivityStream = exchange.getIn().getBody(String.class);
    final byte[] flatJson = JsonFlattener.flatten(jsonActivityStream).getBytes(
        StandardCharsets.UTF_8);
    final InputStream jsonInput = new ByteArrayInputStream(flatJson);
    final String uriString = solrScheme + ":" + separator + separator + solrHost + ":"
        + solrPort + separator + solrContext + separator + collection + "/update/json/docs?commit=true";
    client.post(uriString, jsonInput, "application/json");
  }
}
