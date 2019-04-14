package staatsbibliothek.berlin.hsp.indexUpdateService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import staatsbibliothek.berlin.hsp.indexUpdateService.kafka.SolrClientProperties;

@SpringBootApplication
@EnableConfigurationProperties(SolrClientProperties.class)
public class IndexUpdateServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(IndexUpdateServiceApplication.class, args);
  }
}
