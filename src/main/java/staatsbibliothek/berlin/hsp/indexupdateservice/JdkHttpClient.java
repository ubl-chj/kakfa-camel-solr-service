package staatsbibliothek.berlin.hsp.indexupdateservice;

import java.io.InputStream;

public interface JdkHttpClient {

  /**
   * post.
   *
   * @param uriString  a resource identifier
   * @param stream      an {@link InputStream}
   * @param contentType a content type
   * @throws Exception an URISyntaxException, IOException or InterruptedException
   */
  void post(final String uriString, final InputStream stream, final String contentType) throws Exception;
}
