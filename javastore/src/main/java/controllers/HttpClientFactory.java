package controllers;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;

public class HttpClientFactory {
  public static HttpClient createHttpClient() {
    PoolingClientConnectionManager clientConnectionManager = new PoolingClientConnectionManager();
    clientConnectionManager.setDefaultMaxPerRoute(1000);
    clientConnectionManager.setMaxTotal(1000);
    return new DefaultHttpClient(clientConnectionManager);
  }
}
