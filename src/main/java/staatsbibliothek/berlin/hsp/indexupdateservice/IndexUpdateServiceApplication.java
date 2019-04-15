package staatsbibliothek.berlin.hsp.indexupdateservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(SolrClientProperties.class)
public class IndexUpdateServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(IndexUpdateServiceApplication.class, args);
  }
}
