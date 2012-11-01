package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

public class SearchServlet extends HttpServlet {
  private HttpClient client;
  private ObjectMapper mapper = new ObjectMapper();

  @Override
  public void init() throws ServletException {
    client = HttpClientFactory.createHttpClient();
  }

  @Override
  public void destroy() {
    client.getConnectionManager().shutdown();
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpGet get = new HttpGet("http://localhost:9001/search?query=" + req.getParameter("query"));
    try {
      HttpResponse response = client.execute(get);
      if (response.getStatusLine().getStatusCode() == 200) {
        JsonNode json = mapper.readTree(response.getEntity().getContent());
        JsonNode results = json.get("results");
        resp.setStatus(200);
        Writer writer = resp.getWriter();
        writer.write("Found results: ");
        String sep = "";
        for (JsonNode result : results) {
          writer.write(sep);
          writer.write("\"" + result.get("description").asText() + "\"");
          sep = ", ";
        }
        writer.write(".\n");
      } else {
        EntityUtils.consume(response.getEntity());
        resp.setStatus(500);
        resp.getWriter().println("Error calling search service.");
      }
    } finally {
      get.releaseConnection();
    }
  }
}
