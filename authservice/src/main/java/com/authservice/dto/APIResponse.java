package com.authservice.dto;

@lombok.Data
public class APIResponse<T> {
	
	private String message;
	
	private Integer status;
	
	private T Data;
}
