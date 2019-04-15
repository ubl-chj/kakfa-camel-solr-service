package staatsbibliothek.berlin.hsp.indexupdateservice;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "solr")
public class SolrClientProperties {
  private String host;
  private Integer port;
  private String scheme;
  private String context;
  private String core;

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  public String getScheme() {
    return scheme;
  }

  public void setScheme(String scheme) {
    this.scheme = scheme;
  }

  public String getContext() {
    return context;
  }

  public void setContext(String context) {
    this.context = context;
  }

  public String getCore() {
    return core;
  }

  public void setCore(String core) {
    this.core = core;
  }

}
