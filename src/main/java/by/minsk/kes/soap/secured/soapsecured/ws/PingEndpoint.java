package by.minsk.kes.soap.secured.soapsecured.ws;

import by.minsk.kes.soap.secured.soapsecured.model.PingDetails;
import by.minsk.kes.soap.secured.soapsecured.model.PingRequest;
import by.minsk.kes.soap.secured.soapsecured.model.PingResponse;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class PingEndpoint {

  @PayloadRoot(namespace = "http://byminsk.kes/soapsecured", localPart = "PingRequest")
  @ResponsePayload
  public PingResponse ping(@RequestPayload PingRequest request) {
    final PingResponse response = new PingResponse();
    final PingDetails pingDetails = new PingDetails();
    pingDetails.setResult(request.getText() + "---response");
    response.setResult(pingDetails);
    return response;
  }

}
