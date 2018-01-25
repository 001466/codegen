package com.codash.cgm.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.codash.cgm.db.MapCmd;

public class CodegenException extends Exception {
	Log log=LogFactory.getLog(CodegenException.class);  

	/**
	 * 
	 */
	private static final long serialVersionUID = 1226430826318382469L;

	public CodegenException(String msg) {
		super(msg);
		log.error(msg);
	}

	public CodegenException(String msg, Throwable throwable) {
		super(msg, throwable);
		throwable.printStackTrace();
		log.error(msg);
		log.error(throwable.getMessage());

	}

	public CodegenException(Throwable throwable) {
		super(throwable);
		throwable.printStackTrace();
		log.error(throwable.getMessage());
	}
}
