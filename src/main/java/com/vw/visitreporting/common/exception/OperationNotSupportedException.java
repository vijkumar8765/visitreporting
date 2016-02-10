package com.vw.visitreporting.common.exception;

public class OperationNotSupportedException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ExceptionCode exceptionCode;

	public OperationNotSupportedException(ExceptionCode exceptionCode, String exceptionMessage, Throwable wrappedException ){
		super(exceptionMessage,wrappedException);
		this.exceptionCode = exceptionCode;
	}
	
	public OperationNotSupportedException(ExceptionCode exceptionCode, String exceptionMessage){
		super(exceptionMessage);
		this.exceptionCode = exceptionCode;
	}
	
	public ExceptionCode getExceptionCode() {
		return exceptionCode;
	}

	public void setExceptionCode(ExceptionCode exceptionCode) {
		this.exceptionCode = exceptionCode;
	}
}
