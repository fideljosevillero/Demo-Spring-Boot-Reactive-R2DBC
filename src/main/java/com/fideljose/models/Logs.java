package com.fideljose.models;

import java.util.Date;

import javax.validation.constraints.NotEmpty;

import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Logs {

	@Id
	private Long id;
	
	@NotEmpty
	private String codeservice;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createAt;
	
	private String trace; // UUID
	
	@NotEmpty
	private String statusHttp;
	
	@NotEmpty
	private String message;
	
	public Logs(String codeservice, String statusHttp, String message) {
		this.codeservice = codeservice;
		this.statusHttp = statusHttp;
		this.message = message;
	}
}


//CREATE TABLE logs (
//		ID serial PRIMARY KEY,
//		codeservice VARCHAR (255) NOT NULL,
//		trace VARCHAR (255) NOT NULL,
//		create_at DATE NOT NULL DEFAULT CURRENT_DATE,
//		status_http VARCHAR (255),
//		message VARCHAR (255)
//	);
