package org.cloudfoundry.samples.music.config.data;

import org.apache.coyote.http2.Http2Protocol;

import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class H2cConfig {
    @Bean
    public TomcatConnectorCustomizer customizer() {
      return (connector) -> connector.addUpgradeProtocol(new Http2Protocol());
    }
}
