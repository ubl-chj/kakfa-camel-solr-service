package staatsbibliothekberlin.hsp.indexUpdateService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ElasticSearchProperties.class)
public class IndexUpdateServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IndexUpdateServiceApplication.class, args);
	}

}
