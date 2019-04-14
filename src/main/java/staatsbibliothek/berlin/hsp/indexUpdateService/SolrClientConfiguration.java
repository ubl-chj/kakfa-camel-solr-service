package staatsbibliothek.berlin.hsp.indexUpdateService;

import static java.io.File.separator;

import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SolrClientConfiguration {
  private final String urlString;

  @Autowired
  public SolrClientConfiguration(SolrClientProperties solrClientProperties) {
    final String host = solrClientProperties.getHost();
    final Integer port = solrClientProperties.getPort();
    final String scheme = solrClientProperties.getScheme();
    final String context = solrClientProperties.getContext();
    this.urlString = scheme + ":" + separator + separator + host + ":"
        + port + separator + context;
  }

  @Bean
  public HttpSolrClient solrClient() {
    return new HttpSolrClient.Builder(this.urlString).build();
  }
}
