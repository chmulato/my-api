package com.caracore.myapi.config;

import java.io.File;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConnectorConfig {

    @Value("${cap30.https.keystore.file:}")
    private String keystoreFile;
    @Value("${cap30.https.keystore.password:}")
    private String keystorePass;
    @Value("${cap30.https.keystore.type:}")
    private String keystoreType;
    @Value("${cap30.https.key.password:}")
    private String keyPass;
    @Value("${cap30.https.key-alias:}")
    private String keyAlias;
    @Value("${cap30.https.port:8438}")
    private int tlsPort;


    private Connector createSslConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol"); 
        Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler(); 
        final String absoluteKeystoreFilePath = new File(keystoreFile).getAbsolutePath(); 

        connector.setScheme("https");
        connector.setSecure(true);
        connector.setPort(tlsPort);
        protocol.setKeystoreType(keystoreType);
        protocol.setSSLEnabled(true);
        protocol.setKeystoreFile(absoluteKeystoreFilePath);
        protocol.setKeystorePass(keystorePass);
        protocol.setKeyPass(keyPass);
        protocol.setTruststorePass(keystorePass);
        protocol.setKeyAlias(keyAlias);
        return connector;
    }

    @Bean
    public ServletWebServerFactory servletContainer() { 
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addAdditionalTomcatConnectors(createSslConnector());
        return tomcat;
    }
}