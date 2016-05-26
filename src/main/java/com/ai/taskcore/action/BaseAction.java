package com.ai.taskcore.action;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import com.ai.common.xml.bean.Output;
import com.ai.common.xml.util.ControlConstants;
import com.ai.frame.bean.InputObject;
import com.ai.frame.bean.OutputObject;
import com.ai.frame.logger.Logger;
import com.ai.frame.logger.LoggerFactory;
import com.ai.frame.util.JsonUtil;
import com.ai.taskcore.util.StringUtil;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * Action基类
 */
public abstract class BaseAction extends ActionSupport implements BeanFactoryAware{
	private static final long serialVersionUID = 1581119741116374826L;
	private static final Logger logger = LoggerFactory.getActionLog(BaseAction.class);
	private InputObject inputObject;
	private BeanFactory factory;

	public void setBeanFactory(BeanFactory factory) {
		this.factory = factory;
	}

	/** Get the request Object **/
	public HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	/** Get the response Object **/
	public HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}

	/** Get the current Session **/
	public HttpSession getSession() {
		return getRequest().getSession();
	}
	
	/** Get the current Session **/
	public HttpSession getSession(boolean arg0) {
		return getRequest().getSession(arg0);
	}

	/** Get the Servlet Context **/
	public ServletContext getServletContext() {
		return ServletActionContext.getServletContext();
	}

	/** Get the Value Stack of Struts2 **/
	public ValueStack getValueStack() {
		return ServletActionContext.getValueStack(getRequest());
	}
	
	/** Print OutputStream to the Browser **/
	public void sendJson(String json) {
		try {
			getResponse().setContentType("text/html");
			getResponse().getWriter().print(json);
			logger.info("sendJson", json);
		} catch (IOException e) {
			logger.error("sendJson", "Exception Occured When Send Json to Client !", e);
		}
	}

	/** Get InputObject Object Encapsulated **/
	public InputObject getInputObject() {
		inputObject = (InputObject) ServletActionContext.getContext().get(ControlConstants.INPUTOBJECT);
		return inputObject;
	}

	/** Call Services and Get OutputObject Object **/
	public OutputObject getOutputObject() {
		return getOutputObject(getInputObject());
	}

	/** Call Services and Get OutputObject Object **/
	public OutputObject getOutputObject(InputObject inputObject) {
		OutputObject object = null;
		object = this.execute(inputObject);
		return object;
	}
	
	private OutputObject execute(InputObject inputObject) {
		OutputObject outputObject = new OutputObject(ControlConstants.RETURN_CODE.IS_OK);
		long start = System.currentTimeMillis();
		try {
			logger.info("INVOKE START!", "service=" + inputObject.getService() + ", method=" + inputObject.getMethod());
			Object object = factory.getBean(inputObject.getService());
			Method mth = object.getClass().getMethod(inputObject.getMethod(), InputObject.class, OutputObject.class);
			mth.invoke(object, inputObject ,outputObject);
			logger.info("INVOKE SECCESS!", "COST="+(System.currentTimeMillis() - start)+"ms");
		} catch (Exception e) {
			// 异常处理
			outputObject.setReturnCode(ControlConstants.RETURN_CODE.SYSTEM_ERROR);
			outputObject.setReturnMessage(e.getMessage() == null ? e.getCause().getMessage() : e.getMessage());
			logger.error("", "Invoke Service Error.", inputObject.getService() + "." + inputObject.getMethod(), e);
		}
		return outputObject;
	}
	
	/**
	 * Json String Unified Conversion Method
	 * @param outputObject
	 * @return Json
	 */
	public String convertOutputObject2Json(OutputObject outputObject) {
		return convertOutputObject2Json(getInputObject(), outputObject);
	}

	/**
	 * Json String Unified Conversion Method
	 * @param outputObject
	 * @return Json
	 */
	public String convertOutputObject2Json(InputObject inputObject,OutputObject outputObject) {
		String json = "";
		if (outputObject == null || inputObject == null){
			return json;
		}

		Output output = (Output) ServletActionContext.getContext().get(ControlConstants.OUTPUT);
		try {
			// 如果配置了相应的IConvertor则执行，否则执行默认的Json转换功能
			if (output != null && StringUtil.isNotEmpty(output.getConvertor())) {
				json = JsonUtil.outputObject2Json(output.getConvertor(),
						output.getMethod(), inputObject, outputObject);
			}else{
				json = JsonUtil.outputObject2Json(outputObject);
			}
		} catch (Exception e) {
			logger.error("convertOutputObject", "Convert Output Error.", "", e);
		}
		return json;
	}
	
	protected void convertOutputError(OutputObject outputObject, Exception e) {
		outputObject.setReturnCode(ControlConstants.RETURN_CODE.SYSTEM_ERROR);
		outputObject.setReturnMessage(e.getMessage() == null ? (e.getCause() == null ? "系统异常!"
						: e.getCause().getMessage()) : e.getMessage());
		logger.error("", outputObject.getReturnMessage(), e);
	}
	
	protected void convertOutputError(OutputObject outputObject, String errMsg, Exception e) {
		outputObject.setReturnCode(ControlConstants.RETURN_CODE.SYSTEM_ERROR);
		String exceptionMsg = e.getMessage() == null ? (e.getCause() == null ? "系统异常!"
				: e.getCause().getMessage()) : e.getMessage();
		
		outputObject.setReturnMessage(StringUtil.isNotEmpty(errMsg)? errMsg : exceptionMsg);
		logger.error("", outputObject.getReturnMessage(), e);
	}
	
	
	
}
