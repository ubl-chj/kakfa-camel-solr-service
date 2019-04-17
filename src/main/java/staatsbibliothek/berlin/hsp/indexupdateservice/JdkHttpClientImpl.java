package staatsbibliothek.berlin.hsp.indexupdateservice;

import static java.net.http.HttpClient.Redirect.ALWAYS;
import static java.net.http.HttpClient.Version.HTTP_1_1;
import static java.net.http.HttpClient.Version.HTTP_2;
import static java.net.http.HttpRequest.BodyPublishers.ofInputStream;
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static java.util.Objects.requireNonNull;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.SSLContext;

import org.slf4j.Logger;

public class JdkHttpClientImpl implements JdkHttpClient {

  private static final Logger log = getLogger(JdkHttpClientImpl.class);
  private static final String NON_NULL_IDENTIFIER = "Identifier may not be null!";
  private static HttpClient client = null;

  private JdkHttpClientImpl(final HttpClient client) {
    requireNonNull(client, "HTTP client may not be null!");
    JdkHttpClientImpl.client = client;
  }

  /**
   * LdpClientImpl.
   */
  public JdkHttpClientImpl() {
    this(getClient());
  }

  /**
   * LdpClientImpl.
   *
   * @param sslContext an {@link SSLContext}
   */
  public JdkHttpClientImpl(final SSLContext sslContext) {
    this(getH2Client(sslContext));
  }

  static HttpClient getH2Client(final SSLContext sslContext) {
    final ExecutorService exec = Executors.newCachedThreadPool();
    return HttpClient.newBuilder().executor(exec).sslContext(sslContext)
        .followRedirects(ALWAYS).version(HTTP_2).build();
  }

  static HttpClient getClient() {
    final ExecutorService exec = Executors.newCachedThreadPool();
    return HttpClient.newBuilder().executor(exec).followRedirects(ALWAYS).version(HTTP_1_1).build();
  }

  @Override
  public void post(final String uriString, final InputStream stream, final String contentType) throws
      Exception {
    try {
      requireNonNull(uriString, NON_NULL_IDENTIFIER);
      final URI uri = new URI(uriString);
      final HttpRequest req = HttpRequest.newBuilder(uri).headers("Content-Type", contentType).POST(
          ofInputStream(() -> stream)).build();
      final HttpResponse<String> response = client.send(req, ofString());
      log.info(response.version() + " POST request to {} returned {}", uriString, response.statusCode());
    } catch (Exception ex) {
      throw new Exception(ex.toString(), ex.getCause());
    }
  }
}
