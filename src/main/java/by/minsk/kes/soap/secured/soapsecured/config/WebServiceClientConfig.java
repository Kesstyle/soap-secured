package by.minsk.kes.soap.secured.soapsecured.config;

import by.minsk.kes.soap.secured.soapsecured.client.WebServiceGateway;
import by.minsk.kes.soap.secured.soapsecured.client.XwssMessageSigner;
import by.minsk.kes.soap.secured.soapsecured.model.PingRequest;
import by.minsk.kes.soap.secured.soapsecured.model.PingResponse;
import com.sun.xml.messaging.saaj.soap.ver1_1.SOAPMessageFactory1_1Impl;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.soap.security.support.KeyStoreFactoryBean;
import org.springframework.ws.soap.security.xwss.callback.KeyStoreCallbackHandler;
import org.springframework.ws.transport.http.CommonsHttpMessageSender;

@Configuration
public class WebServiceClientConfig {

  @Bean
  public WebServiceGateway wsGateway() throws Exception {
    final WebServiceGateway wsGateway = new WebServiceGateway(messageFactory());
    wsGateway.setDefaultUri("http://localhost:5002/ws/ping");
    wsGateway.setXwssMessageSigner(clientMessageSigner());
    wsGateway.setMarshaller(marshaller());
    wsGateway.setUnmarshaller(marshaller());
    wsGateway.setMessageSender(new CommonsHttpMessageSender(httpClient()));
    return wsGateway;
  }

  @Bean
  public SaajSoapMessageFactory messageFactory() {
    final SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();
    messageFactory.setMessageFactory(new SOAPMessageFactory1_1Impl());
    return messageFactory;
  }

  @Bean
  public XwssMessageSigner clientMessageSigner() throws Exception {
    return new XwssMessageSigner(new ClassPathResource("client/securityPolicy-client.xml"),
        clientCallbackHandler());
  }

  @Bean
  KeyStoreCallbackHandler clientCallbackHandler() {
    final KeyStoreCallbackHandler callbackHandler = new KeyStoreCallbackHandler();
    callbackHandler.setKeyStore(clientTrustStore().getObject());
    callbackHandler.setDefaultAlias("kes-client");
    callbackHandler.setPrivateKeyPassword("changeme");
    return callbackHandler;
  }

  @Bean
  KeyStoreFactoryBean clientTrustStore() {
    final KeyStoreFactoryBean keyStoreFactoryBean = new KeyStoreFactoryBean();
    keyStoreFactoryBean.setLocation(new ClassPathResource("client/kes-client.jks"));
    keyStoreFactoryBean.setPassword("changeme");
    return keyStoreFactoryBean;
  }

  @Bean
  Jaxb2Marshaller marshaller() {
    final Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
    marshaller.setClassesToBeBound(classesToBeBound());
    return marshaller;
  }

  private HttpClient httpClient() {
    final HttpClient httpClient = new HttpClient();
    httpClient.setTimeout(10000);
    httpClient.setConnectionTimeout(10000);
    httpClient.setHttpConnectionManager(connectionManager());
    return httpClient;
  }

  private MultiThreadedHttpConnectionManager connectionManager() {
    final MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
    connectionManager.setMaxConnectionsPerHost(40);
    connectionManager.setMaxTotalConnections(600);
    return connectionManager;
  }

  private Class[] classesToBeBound() {
    return new Class[] {PingRequest.class, PingResponse.class};
  }
}
