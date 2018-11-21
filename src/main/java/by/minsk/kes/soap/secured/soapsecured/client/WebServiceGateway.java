package by.minsk.kes.soap.secured.soapsecured.client;

import by.minsk.kes.soap.secured.soapsecured.model.PingRequest;
import by.minsk.kes.soap.secured.soapsecured.model.PingResponse;

import org.springframework.core.io.Resource;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.SoapFaultClientException;
import org.springframework.xml.transform.ResourceSource;

import java.io.IOException;

import javax.xml.transform.Source;

public class WebServiceGateway extends WebServiceGatewaySupport {

  private XwssMessageSigner xwssMessageSigner;

  public WebServiceGateway() {}

  public WebServiceGateway(final WebServiceMessageFactory messageFactory) {
    super(messageFactory);
  }

  public PingResponse callService(final PingRequest request) throws IOException {
    try {
      final PingResponse response = (PingResponse) getWebServiceTemplate()
          .marshalSendAndReceive(request, xwssMessageSigner.getCallback());
      return response;
    } catch (SoapFaultClientException e) {
      // error handling
    }
    return null;
  }

  public void callService(final Resource request) throws IOException {
    Source requestSource = new ResourceSource(request);
    try {
      getWebServiceTemplate()
          .sendSourceAndReceive(
              requestSource,
              xwssMessageSigner.getCallback(),
              new MySourceExtractor());
    } catch (SoapFaultClientException e) {
      // error handling
    }
  }

  public void setXwssMessageSigner(XwssMessageSigner signer) {
    this.xwssMessageSigner = signer;
  }
}
