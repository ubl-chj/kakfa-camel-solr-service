package staatsbibliothek.berlin.hsp.indexUpdateService.kafka;

import org.apache.solr.client.solrj.beans.Field;

public class Target {
    @Field
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Field
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Field
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
