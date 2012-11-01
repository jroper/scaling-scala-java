package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PaymentsServlet extends HttpServlet {
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
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpPost post = new HttpPost("http://localhost:9002/payments");
    JsonNode json = mapper.createObjectNode().put("amount", Integer.parseInt(req.getParameter("amount")));
    post.setEntity(new StringEntity(mapper.writeValueAsString(json)));
    post.setHeader("Content-Type", "application/json");
    try {
      HttpResponse response = client.execute(post);
      EntityUtils.toString(response.getEntity());
      if (response.getStatusLine().getStatusCode() == 200) {
        resp.setStatus(200);
        resp.getWriter().println("Payment processed.");
      } else {
        resp.setStatus(500);
        resp.getWriter().println("Error calling payment service.");
      }
    } finally {
      post.releaseConnection();
    }
  }
}
