/*
 * Created on 2005-6-22
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.codash.cgm.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Administrator
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class Logger {
	private Log log = null;
	static{
		Log4jConfig.load();//装载log4j配置文件
	}
	private Logger() {
		log = LogFactory.getLog(this.getClass());
	}

	private Logger(Class c) {
		log = LogFactory.getLog(c);
	}

	private Logger(String className) {
		log = LogFactory.getLog(className);
	}

	public static Logger getLogger() {
		return new Logger();
	}

	public static Logger getLogger(Class c) {
		
		return new Logger(c);
	}

	public static Logger getLogger(String className) {
		return new Logger(className);
	}

	public void trace(String info) {
		if (log.isTraceEnabled())
			log.trace(info);
	}

	public void debug(String info) {
		if (log.isDebugEnabled())
			log.debug(info);
	}

	public void info(String info) {
		if (log.isInfoEnabled())
			log.info(info);
	}

	public void warn(String info) {
		if (log.isWarnEnabled())
			log.warn(info);
	}

	public void error(String info) {
		if (log.isErrorEnabled())
			log.error(info);
	}

	public void fatal(String info) {
		if (log.isFatalEnabled())
			log.fatal(info);
	}

	public boolean isTraceEnabled() {
		return log.isTraceEnabled();
	}

	public boolean isDebugEnabled() {
		return log.isDebugEnabled();
	}

	public boolean isInfoEnabled() {
		return log.isInfoEnabled();
	}

	public boolean isWarnEnabled() {
		return log.isWarnEnabled();
	}

	public boolean isErrorEnabled() {
		return log.isErrorEnabled();
	}

	public boolean isFatalEnabled() {
		return log.isFatalEnabled();
	}
}
