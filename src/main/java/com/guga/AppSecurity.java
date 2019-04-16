package com.guga;

import io.javalin.Javalin;
import org.eclipse.jetty.alpn.server.ALPNServerConnectionFactory;
import org.eclipse.jetty.http2.HTTP2Cipher;
import org.eclipse.jetty.http2.server.HTTP2ServerConnectionFactory;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.util.ssl.SslContextFactory;

// Example from https://github.com/tipsy/javalin-http2-example
public class AppSecurity {
    public static void main(String[] args) {

        final Javalin app = Javalin.create();
        app
                .server(AppSecurity::createHttp2Server)
                .enableStaticFiles("/public")
                .get("/", ctx -> ctx.result("Hello World"))
                .start();
    }

    private static Server createHttp2Server() {
        Server server = new Server();

        ServerConnector connector = new ServerConnector(server);
        //  Set the port otherwise 55571
        connector.setPort(8080);
        server.addConnector(connector);

        // HTTP Configuration
        HttpConfiguration httpConfig = new HttpConfiguration();
        httpConfig.setSendServerVersion(false);
        httpConfig.setSecureScheme("https");
        httpConfig.setSecurePort(8443);

        // SSL Context Factory for HTTPS and HTTP/2
        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStorePath(AppSecurity.class.getResource("/keystore.jks").toExternalForm()); // replace with your real keystore
        sslContextFactory.setKeyStorePassword("123456"); // replace with your real password
        sslContextFactory.setCipherComparator(HTTP2Cipher.COMPARATOR);
        sslContextFactory.setProvider("Conscrypt");

        // HTTPS Configuration
        HttpConfiguration httpsConfig = new HttpConfiguration(httpConfig);
        httpsConfig.addCustomizer(new SecureRequestCustomizer());

        // HTTP/2 Connection Factory
        HTTP2ServerConnectionFactory http2ServerConnectionFactory = new HTTP2ServerConnectionFactory(httpsConfig);
        final ALPNServerConnectionFactory alpnServerConnectionFactory = new ALPNServerConnectionFactory();
        alpnServerConnectionFactory.setDefaultProtocol("http2ServerConnectionFactory");

        // SSL Connection Factory
        SslConnectionFactory ssl = new SslConnectionFactory(sslContextFactory, alpnServerConnectionFactory.getProtocol());

        // HTTP/2 Connector
        ServerConnector http2Connector = new ServerConnector(server,
                ssl,
                alpnServerConnectionFactory,
                http2ServerConnectionFactory,
                new HttpConnectionFactory(httpsConfig));

        // Set the port otherwise 55572
        http2Connector.setPort(8443);
        server.addConnector(http2Connector);

        return server;
    }

}
