package by.minsk.kes.soap.secured.soapsecured.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.security.support.KeyStoreFactoryBean;
import org.springframework.ws.soap.security.xwss.XwsSecurityInterceptor;
import org.springframework.ws.soap.security.xwss.callback.KeyStoreCallbackHandler;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import java.security.KeyStore;
import java.util.List;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

  @Bean
  public ServletRegistrationBean messageDispatcherServlet(final ApplicationContext context) {
    final MessageDispatcherServlet messageDispatcherServlet = new MessageDispatcherServlet();
    messageDispatcherServlet.setApplicationContext(context);
    messageDispatcherServlet.setTransformWsdlLocations(true);
    return new ServletRegistrationBean(messageDispatcherServlet, "/ws/*");
  }

  @Bean(name = "ping")
  public DefaultWsdl11Definition defaultWsdl11Definition(final XsdSchema pingSchema) {
    final DefaultWsdl11Definition definition = new DefaultWsdl11Definition();
    definition.setPortTypeName("PingKesPort");
    definition.setTargetNamespace("http://byminsk.kes/soapsecured");
    definition.setLocationUri("/ws/ping");
    definition.setSchema(pingSchema);
    return definition;
  }

  @Bean
  public XsdSchema pingSchema() {
    return new SimpleXsdSchema(new ClassPathResource("ping.xsd"));
  }

  @Bean
  XwsSecurityInterceptor securityInterceptor() {
    XwsSecurityInterceptor securityInterceptor = new XwsSecurityInterceptor();
    securityInterceptor.setCallbackHandler(callbackHandler());
    securityInterceptor.setPolicyConfiguration(new ClassPathResource("securityPolicy.xml"));
    return securityInterceptor;
  }

  @Bean
  KeyStoreCallbackHandler callbackHandler() {
    final KeyStoreCallbackHandler callbackHandler = new KeyStoreCallbackHandler();
    callbackHandler.setTrustStore((KeyStore) trustStore().getObject());
    return callbackHandler;
  }

  @Bean
  KeyStoreFactoryBean trustStore() {
    final KeyStoreFactoryBean keyStoreFactoryBean = new KeyStoreFactoryBean();
    keyStoreFactoryBean.setLocation(new ClassPathResource("/WEB-INF/MyTruststore.jks"));
    keyStoreFactoryBean.setPassword("changeme");
    return keyStoreFactoryBean;
  }

  @Override
  public void addInterceptors(final List<EndpointInterceptor> interceptors) {
    interceptors.add(securityInterceptor());
  }
}
