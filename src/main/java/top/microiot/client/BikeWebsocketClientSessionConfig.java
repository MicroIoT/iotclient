package top.microiot.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import top.microiot.api.client.HttpClientSession;
import top.microiot.api.client.WebsocketClientSession;

@Configuration
public class BikeWebsocketClientSessionConfig {
	@Bean("bikeWebsocketClientSession")
	@Scope("prototype")
	public WebsocketClientSession bikeWebsocketClientSession(@Qualifier("bikeHttpClientSession")HttpClientSession httpClientSession, WebSocketStompClient websocketStompClient) {
		return new WebsocketClientSession(httpClientSession, websocketStompClient);
	}
}
