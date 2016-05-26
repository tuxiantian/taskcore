package com.ai.taskcore.action;
import com.ai.frame.bean.OutputObject;
import com.ai.frame.logger.Logger;
import com.ai.frame.logger.LoggerFactory;

/**
 * 通用类
 */
public class CommonAction extends BaseAction {
	private static final long serialVersionUID = 2021751417577646314L;
	private static final Logger logger = LoggerFactory.getActionLog(CommonAction.class);
	
	/** Uniform Method Invocation **/
	public String execute() {
		logger.info("execute", "Start");
		OutputObject object = super.getOutputObject();
		super.sendJson(super.convertOutputObject2Json(object));
		logger.info("execute", "End");
		return null;
	}
}
