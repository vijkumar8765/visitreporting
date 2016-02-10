package com.vw.visitreporting.common.exception;

public enum ExceptionCode {


	
	ACCESS_CONTROL_HANDLER_OPERATION_NOT_SUPPORTED(1, "hasPermission method in access control handler is not supported"),
	AUDIT_RECORD_ENTITY_CLASS_NOT_FOUND(2,"In Audit Record Entity class not found"),
	GENERAL(999, "The associated message with exception describes the issue");

	
	private final int code;
	private final String message;
	
	private ExceptionCode(int code, String message){
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
	
	
}
