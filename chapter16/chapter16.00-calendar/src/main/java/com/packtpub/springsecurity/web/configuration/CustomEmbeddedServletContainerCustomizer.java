package com.packtpub.springsecurity.web.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"HTTP", "default"})
public final class CustomEmbeddedServletContainerCustomizer implements EmbeddedServletContainerCustomizer {

    @Value("${server.port:8080}")
    private int serverPort;

    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {

        container.setPort(serverPort);

    }
} // The End...
