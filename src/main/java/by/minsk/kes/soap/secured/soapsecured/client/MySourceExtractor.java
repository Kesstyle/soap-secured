package by.minsk.kes.soap.secured.soapsecured.client;

import org.springframework.ws.client.core.SourceExtractor;

import javax.xml.transform.Source;

public class MySourceExtractor implements SourceExtractor {

  public Object extractData(final Source src) {
    return src;
  }
}