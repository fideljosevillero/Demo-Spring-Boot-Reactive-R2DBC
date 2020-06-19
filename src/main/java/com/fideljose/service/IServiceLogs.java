package com.fideljose.service;

import com.fideljose.models.Logs;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IServiceLogs {
	
	public Flux<Logs> findAll();
	
	public Mono<Logs> findById(Long id);
	
	public Mono<Void> delete(Logs log);
	
	public Mono<Logs> create(Logs log);
	
	
}
