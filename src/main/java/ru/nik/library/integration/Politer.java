package ru.nik.library.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannels;

@ComponentScan
@Configuration
@EnableIntegration
public class Politer {

	@Bean
	public DirectChannel  commentsChannel() {
		return MessageChannels.direct("commentsChannel").get();
	}

	@Bean
	public DirectChannel  politerChannel() {
		return MessageChannels.direct("politerChannel").get();
	}

	@Bean
	public IntegrationFlow channelFlow() {

		return flow -> flow.channel("commentsChannel")
			.handle("commentChanger", "change")
			.channel("politerChannel");
	}

}
