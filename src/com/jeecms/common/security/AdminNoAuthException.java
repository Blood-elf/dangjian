package com.jeecms.common.security;

@SuppressWarnings("serial")
public class AdminNoAuthException extends AuthenticationException{
	public AdminNoAuthException() {
	}

	public AdminNoAuthException(String msg) {
		super(msg);
	}

	public AdminNoAuthException(String msg, Object extraInformation) {
		super(msg, extraInformation);
	}
}
