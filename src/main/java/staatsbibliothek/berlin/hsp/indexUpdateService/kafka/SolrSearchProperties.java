package staatsbibliothek.berlin.hsp.indexUpdateService.kafka;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "solr")
public class SolrSearchProperties {
    private String host;
    private Integer port;
    private String scheme;
    private String context;
    private String core;

    public String getHost() {
        return host;
    }
    public Integer getPort() {
        return port;
    }
    public String getScheme() {
        return scheme;
    }
    public String getContext() {
        return context;
    }
    public String getCore() { return core; }

    public void setHost(String host) {
        this.host = host;
    }
    public void setPort(Integer port) {
        this.port = port;
    }
    public void setScheme(String scheme) {
        this.scheme = scheme;
    }
    public void setContext(String context) {
        this.context = context;
    }
    public void setCore(String core) { this.core = core; }
}
