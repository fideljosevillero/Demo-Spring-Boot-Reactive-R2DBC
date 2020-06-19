package com.fideljose.router;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.fideljose.handler.LogsHandler;

@Configuration
public class RouterFunctionConfig {

	@Value("${url-save-log}")
	private String urlSavelog;
	
	@Bean
	public RouterFunction<ServerResponse> routes(LogsHandler handler){
		return route(GET("/api/"), handler::index)
				.andRoute(GET("/api/logs-list"), handler::list)
				.andRoute(POST(urlSavelog), handler::crear)
				.andRoute(GET("/api/log/{id}"), handler::get)
				.andRoute(PUT("/api/log-edit/{id}"), handler::edit)
				.andRoute(DELETE("/api/log-delete/{id}"), handler::delete);
	}
	
}
