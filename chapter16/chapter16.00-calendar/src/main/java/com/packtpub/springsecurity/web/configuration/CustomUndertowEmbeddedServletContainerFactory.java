package com.packtpub.springsecurity.web.configuration;

import io.undertow.UndertowOptions;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.SecurityInfo;
import io.undertow.servlet.api.TransportGuaranteeType;
import io.undertow.servlet.api.WebResourceCollection;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.undertow.UndertowBuilderCustomizer;
import org.springframework.boot.context.embedded.undertow.UndertowDeploymentInfoCustomizer;
import org.springframework.boot.context.embedded.undertow.UndertowEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * This configuration class will force all HTTP traffic to redirect to HTTPS.
 *
 * @author mickknutson
 * TODO:
 * https://github.com/jhipster/jhipster-sample-app-token/blob/master/src/test/java/io/github/jhipster/sample/config/WebConfigurerTest.java
 * SSL: https://github.com/spring-projects/spring-boot/issues/9351
 * http://www.thedevpiece.com/building-microservices-undertow-cdi-jaxrs/
 * https://www.javatips.net/api/io.undertow.undertow
 */
@Configuration
@Profile("HTTP2")
public class CustomUndertowEmbeddedServletContainerFactory {

    @Bean
    public UndertowEmbeddedServletContainerFactory embeddedServletContainerFactory() {
        UndertowEmbeddedServletContainerFactory factory =
                new UndertowEmbeddedServletContainerFactory();
        factory.addBuilderCustomizers(
                builder -> builder.setServerOption(UndertowOptions.ENABLE_HTTP2, true)
                                .addHttpListener(8080, "0.0.0.0")
        );

//        factory.addBuilderCustomizers(new UndertowBuilderCustomizer() {
//            @Override
//            public void customize(io.undertow.Undertow.Builder builder) {
//                builder.addHttpListener(8080, "0.0.0.0");
//            }
//        });

        return factory;
    }

    /*@Bean
    public EmbeddedServletContainerFactory undertow() {
        UndertowEmbeddedServletContainerFactory undertow = new UndertowEmbeddedServletContainerFactory();
        undertow.addBuilderCustomizers(builder -> builder.addHttpListener(8089, "0.0.0.0"));
        undertow.addDeploymentInfoCustomizers(deploymentInfo -> {
            deploymentInfo.addSecurityConstraint(new SecurityConstraint()
                    .addWebResourceCollection(new WebResourceCollection()
                            .addUrlPattern("/*"))
                    .setTransportGuaranteeType(TransportGuaranteeType.CONFIDENTIAL)
                    .setEmptyRoleSemantic(SecurityInfo.EmptyRoleSemantic.PERMIT))
                    .setConfidentialPortManager(exchange -> 443);
        });
        return undertow;
    }*/

//    @Bean
    public UndertowEmbeddedServletContainerFactory embeddedServletContainerFactory3() {
        UndertowEmbeddedServletContainerFactory factory =
                new UndertowEmbeddedServletContainerFactory();
        factory.addDeploymentInfoCustomizers(new UndertowDeploymentInfoCustomizer() {

            @Override
            public void customize(DeploymentInfo deploymentInfo) {
                deploymentInfo.setResourceManager(
                        new ClassPathResourceManager(deploymentInfo.getClassLoader(),
                                "META-INF/resources"));
            }
        }
        );
        return factory;
    }


    /**
     * Create an HTTP redirect connector
     * NOTE: This only works with HTTP/1.1
     * @return
     */
    private Connector httpsConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(8080);
        connector.setSecure(false);
        connector.setRedirectPort(8443);

        return connector;
    }

} // The End...
