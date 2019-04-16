package staatsbibliothek.berlin.hsp.indexupdateservice;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class ActivityStreamProcessor implements Processor {
  public static final String ACTIVITY_STREAM_ID = "ActivityStreamId";
  public static final String ACTIVITY_STREAM_TYPE = "ActivityStreamType";
  public static final String ACTIVITY_STREAM_NAME = "ActivityStreamName";
  public static final String ACTIVITY_STREAM_ACTOR = "ActivityStreamActor";
  public static final String ACTIVITY_STREAM_INBOX = "ActivityStreamInbox";
  public static final String ACTIVITY_STREAM_OBJECT_ID = "ActivityStreamObjectId";
  public static final String ACTIVITY_STREAM_OBJECT_TYPE = "ActivityStreamObjectType";
  private static final String OBJECT = "object";
  private static final String ID = "id";
  private static final String TYPE = "type";
  private static final String NAME = "name";
  private static final String ACTOR = "actor";
  private static final String INBOX = "inbox";

  public ActivityStreamProcessor() {
  }

  public void process(Exchange exchange) {
    Map body = exchange.getIn().getBody(Map.class);
    setHeader(exchange, ACTIVITY_STREAM_ID, body.get(ID));
    setHeader(exchange, ACTIVITY_STREAM_TYPE, body.get(TYPE));
    if (body.containsKey(OBJECT) && body.get(OBJECT) instanceof Map) {
      final Map object = (Map) body.get(OBJECT);
      setHeader(exchange, ACTIVITY_STREAM_OBJECT_ID, object.get(ID));
      setHeader(exchange, ACTIVITY_STREAM_OBJECT_TYPE, object.get(TYPE));
    }
  }

  private void setHeader(Exchange exchange, String header, Object value) {
    if (Objects.nonNull(
        value) && (value instanceof String || value instanceof List)) {
      exchange.getIn().setHeader(header, value);
    }
  }
}

