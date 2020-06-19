package com.fideljose;

import java.util.Collections;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fideljose.models.Logs;
import com.fideljose.service.IServiceLogs;

import reactor.core.publisher.Mono;

@AutoConfigureWebTestClient
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class DemoSpringBootReactiveR2DbcApplicationTests {

	Long id;
	
	@Before
	public void before() {
		id = 0L;
	}
	
	@Autowired
	private WebTestClient client;
	
	@Autowired
	IServiceLogs service;
	
	@Test
	public void createTest() {
		
		Logs log = new Logs("0170", "205", "org.springframework.beans.factory.UnsatisfiedDependencyException:");
		
		client.post().uri("/api/log")
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.body(Mono.just(log), Logs.class)
		.exchange()
		.expectStatus().isCreated()
		.expectBody(Logs.class)
		.consumeWith(response -> {
			Object o = response.getResponseBody();
			Logs l = new ObjectMapper().convertValue(o, Logs.class);
			Assertions.assertThat(l.getId().toString()).isNotEmpty();
			Assertions.assertThat(l.getCodeservice()).isNotEmpty();
			Assertions.assertThat(l.getTrace()).isNotEmpty();
		});
	}
	
	@Test
	public void listarTest() {
		client.get()
		.uri("/api/logs-list")
		.exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBodyList(Logs.class)
		.consumeWith(response -> {
			List<Logs> logs = response.getResponseBody();
			
			Assertions.assertThat(logs.size()>0).isTrue();
			Assertions.assertThat(logs.get(0).getTrace().length() > 7).isTrue();
		});
	}
	
	@Test
	public void editTest() {
		
		client.get()
		.uri("/api/logs-list")
		.exchange()
		.expectBodyList(Logs.class)
		.consumeWith(response -> {
			List<Logs> logs = response.getResponseBody();
			id = logs.get(0).getId();
		});
		
		Logs logEdit = new Logs("000", "111", "org.springframework.beans.factory.UnsatisfiedDependencyException:");
		
		client.put()
		.uri("/api/log-edit/{id}", Collections.singletonMap("id", id))
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.body(Mono.just(logEdit), Logs.class)
		.exchange()
		.expectStatus().isCreated()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
//		.expectBody(Logs.class)
//		.consumeWith(response -> {
//			Object o = response.getResponseBody();
//			Logs l = new ObjectMapper().convertValue(o, Logs.class);
//			
//			Assertions.assertThat(l.getId()).isEqualTo(id);
//			Assertions.assertThat(l.getCodeservice()).isEqualTo("000");
//			Assertions.assertThat(l.getMessasge()).isEqualTo("org.springframework.beans.factory.UnsatisfiedDependencyException:");
//		});
		.expectBody()
		.jsonPath("$.id").isNotEmpty()
		.jsonPath("$.codeservice").isEqualTo("000")
		.jsonPath("$.message").isEqualTo("org.springframework.beans.factory.UnsatisfiedDependencyException:");
	}
	
	
	@Test
	public void getTest() {
		
		client.get()
		.uri("/api/logs-list")
		.exchange()
		.expectBodyList(Logs.class)
		.consumeWith(response -> {
			List<Logs> logs = response.getResponseBody();
			id = logs.get(0).getId();
		});
		
		client.get()
		.uri("/api/log/{id}", Collections.singletonMap("id", id))
		.exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBodyList(Logs.class)
		.consumeWith(response -> {
			List<Logs> logs = response.getResponseBody();
			
			Assertions.assertThat(logs.size()>0).isTrue();
			Assertions.assertThat(logs.get(0).getTrace().length() > 10).isTrue();
		});
	}
	
	@Test
	public void deleteTest() {
		
		client.get()
		.uri("/api/logs-list")
		.exchange()
		.expectBodyList(Logs.class)
		.consumeWith(response -> {
			List<Logs> logs = response.getResponseBody();
			id = logs.get(0).getId();
		});
		
		client.delete()
		.uri("/api/log-delete/{id}", Collections.singletonMap("id", id))
		.exchange()
		.expectStatus().isNoContent()
		.expectBody().isEmpty();
		
	}
	
	
}
