package com.fideljose.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fideljose.models.Logs;
import com.fideljose.repository.ILogsRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ServiceLogs implements IServiceLogs {

	@Autowired
	private ILogsRepository repository;

	@Override
	public Flux<Logs> findAll() {
		return repository.findAll();
	}

	@Override
	public Mono<Logs> findById(Long id) {
		return repository.findById(id);
	}

	@Override
	public Mono<Void> delete(Logs log) {
		return repository.delete(log);
	}

	@Override
	public Mono<Logs> create(Logs log) {
		return repository.save(log);
	}
	

}
