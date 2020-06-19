package com.fideljose.handler;

import java.net.URI;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.fideljose.models.Logs;
import com.fideljose.service.IServiceLogs;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class LogsHandler {

	@Autowired
	private Validator validator;
	
	@Autowired
	IServiceLogs service;
	
	@Value("${url-save-log}")
	private String urlSavelog;
	
	public Mono<ServerResponse> index(ServerRequest request){
		return ServerResponse
				.ok()
				.bodyValue("Bienvenidos Demo-Spring-Boot-Reactive-R2DBC!!!");
	}
	
	public Mono<ServerResponse> list(ServerRequest request){
		Flux<Logs> people = service.findAll();
		people.subscribe(); //*
    	return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				 .body(people, Logs.class);
	}
	
	public Mono<ServerResponse> crear(ServerRequest request){
		Mono<Logs> Logs = request.bodyToMono(Logs.class);
		
		return Logs.flatMap(log -> {
			
			Errors errors = new BeanPropertyBindingResult(log, Logs.class.getName());
			validator.validate(log, errors);
			
			if(errors.hasErrors()) {
				return Flux.fromIterable(errors.getFieldErrors())
						.map(fieldError -> "El campo " + fieldError.getField() + " " + fieldError.getDefaultMessage())
						.collectList()
						.flatMap(list -> ServerResponse
											.badRequest()
											.contentType(MediaType.APPLICATION_JSON)
											.bodyValue(list));
			} else {
				Mono<Logs> LogsDB = service.create(log);
				log.setTrace(UUID.randomUUID().toString());
				log.setCreateAt(new Date());
				 return ServerResponse
//						 .created(URI.create("/api/Logs"))
						 .created(URI.create(urlSavelog))
						 .contentType(MediaType.APPLICATION_JSON)
						 .body(LogsDB, Logs.class);
			}
			
		});
	}
	
	public Mono<ServerResponse> get(ServerRequest request) {
		Long id = Long.parseLong(request.pathVariable("id"));
		return service.findById(id).flatMap(p -> ServerResponse
					.ok()
					.bodyValue(p))
				.switchIfEmpty(ServerResponse.notFound().build());
	}
	
	public Mono<ServerResponse> edit(ServerRequest request) {
		Mono<Logs> Logs = request.bodyToMono(Logs.class);
		String pathVar = request.pathVariable("id");
		
		Long id = Long.parseLong(pathVar);
		Mono<Logs> LogsDB = service.findById(id);
		return LogsDB.zipWith(Logs, (db, req) ->{
			db.setCodeservice(req.getCodeservice());
			db.setCreateAt(new Date());
			db.setMessage(req.getMessage());
			db.setStatusHttp(req.getStatusHttp());
			
			db.setTrace(UUID.randomUUID().toString());
			db.setCreateAt(new Date());
			
			return db;
		}).flatMap(l -> ServerResponse
				.created(URI.create(urlSavelog.concat(l.getId().toString())))
				.contentType(MediaType.APPLICATION_JSON)
				.body(service.create(l), Logs.class))
		.switchIfEmpty(ServerResponse.notFound().build());
	}
	
	
	public Mono<ServerResponse> delete(ServerRequest request){
		Long id = Long.parseLong(request.pathVariable("id"));
		Mono<Logs> logDB = service.findById(id);
		
		return logDB.flatMap(l -> service.delete(l))
									.then(ServerResponse
											.noContent()
											.build())
									.switchIfEmpty(ServerResponse.notFound().build());
		
	}
	
}
