package staatsbibliothek.berlin.hsp.indexupdateservice;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.staatsbibliothek.berlin.hsp.domainmodel.entities.Dokument;

@JsonPropertyOrder({"context", "summary", "type", "published", "actor", "object", "target"})
public class ActivityStream {
  @JsonProperty("@context")
  private String context;
  @JsonProperty("id")
  private String id;
  @JsonProperty("summary")
  private String summary;
  @JsonProperty("type")
  private String type;
  @JsonProperty("published")
  private String published;
  @JsonProperty("actor")
  private Actor actor;
  @JsonProperty
  private Object object;
  @JsonProperty
  private Target target;

  public String getContext() {
    return context;
  }

  public void setContext(String context) {
    this.context = context;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getPublished() {
    return published;
  }

  public void setPublished(String published) {
    this.published = published;
  }

  public Actor getActor() {
    return actor;
  }

  public void setActor(Actor actor) {
    this.actor = actor;
  }

  public Object getObject() {
    return object;
  }

  public void setObject(Object object) {
    this.object = object;
  }

  public Target getTarget() {
    return target;
  }

  public void setTarget(Target target) {
    this.target = target;
  }

  public static class Actor {
    @JsonProperty
    private Double type;
    @JsonProperty
    private String id;
    @JsonProperty
    private String name;
    @JsonProperty
    private String url;

    public Double getType() {
      return type;
    }

    public void setType(Double type) {
      this.type = type;
    }

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getUrl() {
      return url;
    }

    public void setUrl(String url) {
      this.url = url;
    }
  }

  public static class Object {
    @JsonProperty
    private String type;
    @JsonProperty
    private String id;
    @JsonProperty
    private String name;
    @JsonProperty
    private String url;
    @JsonProperty
    private Dokument content;

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
    }

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getUrl() {
      return url;
    }

    public void setUrl(String url) {
      this.url = url;
    }

    public Dokument getContent() {
      return this.content;
    }

    public void setContent(Dokument content) {
      this.content = content;
    }
  }

  public static class Target {
    @JsonProperty
    private String type;
    @JsonProperty
    private String id;
    @JsonProperty
    private String name;

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
    }

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }
}

