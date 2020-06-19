package com.fideljose.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.fideljose.models.Logs;


public interface ILogsRepository extends ReactiveCrudRepository<Logs, Long> {

}
