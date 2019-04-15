package staatsbibliothek.berlin.hsp.indexupdateservice;

import java.util.List;
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
    ActivityStream body = exchange.getIn().getBody(ActivityStream.class);
    this.setHeader(exchange, "ActivityStreamId", body.getId());
    this.setHeader(exchange, "ActivityStreamObjectId", body.getObjectId());
    this.setHeader(exchange, "ActivityStreamType", body.getType());
  }

  private void setHeader(Exchange exchange, String header, Object value) {
    if (Objects.nonNull(
        value) && (value instanceof String || value instanceof List)) {
      exchange.getIn().setHeader(header, value);
    }
  }
}

