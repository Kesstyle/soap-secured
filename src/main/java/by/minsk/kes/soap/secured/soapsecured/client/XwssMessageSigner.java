package by.minsk.kes.soap.secured.soapsecured.client;

import com.sun.xml.wss.ProcessingContext;
import com.sun.xml.wss.XWSSProcessor;
import com.sun.xml.wss.XWSSProcessorFactory;

import org.springframework.core.io.Resource;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.ws.soap.security.xwss.callback.KeyStoreCallbackHandler;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.soap.SOAPMessage;

public class XwssMessageSigner {

  private final XWSSProcessor processor;

  public XwssMessageSigner(Resource policyFile, KeyStoreCallbackHandler keystoreHandler) throws Exception {

    InputStream in = policyFile.getInputStream();
    XWSSProcessorFactory factory = XWSSProcessorFactory.newInstance();
    processor = factory.createProcessorForSecurityConfiguration(in, keystoreHandler);
    in.close();
  }

  public WebServiceMessageCallback getCallback() {
    return message -> {
          SaajSoapMessage origSaajMessage = (SaajSoapMessage) message;
          SOAPMessage origSoapMessage = origSaajMessage.getSaajMessage();
          ProcessingContext context = new ProcessingContext();
          try {
            context.setSOAPMessage(origSoapMessage);
            SOAPMessage securedSoapMessage = processor.secureOutboundMessage(context);
            origSaajMessage.setSaajMessage(securedSoapMessage);
          } catch (Exception exc) {
            exc.printStackTrace();
            throw new IOException(exc.getMessage());
          }
        };
  }
}