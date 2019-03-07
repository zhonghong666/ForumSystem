package com.zhonghong.helper;

/**
 * 只要是 JdbcHelper 类中需要抛出的异常，一律抛出 DataAccessException
 */
public class DataAccessException extends RuntimeException {

	private static final long serialVersionUID = -4383302629624595950L;
	
	public DataAccessException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public DataAccessException(Throwable cause) {
		super(cause);
	}

	public DataAccessException(String message) {
		super(message);
	}

}
