package com.ai.taskcore.interceptor;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.ai.common.xml.bean.Action;
import com.ai.common.xml.bean.Input;
import com.ai.common.xml.bean.Parameter;
import com.ai.common.xml.util.ControlConstants;
import com.ai.common.xml.util.ControlRequestUtil;
import com.ai.frame.bean.InputObject;
import com.ai.frame.logger.Logger;
import com.ai.frame.logger.LoggerFactory;
import com.ai.frame.util.ConvertUtil;
import com.ai.taskcore.util.StringUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * 拦截器
 */
public class AuthorityInterceptor extends AbstractInterceptor {
	private static final long serialVersionUID = 8259007565097878289L;
	private static final Logger logger = LoggerFactory
			.getUtilLog(AuthorityInterceptor.class);

	public String intercept(ActionInvocation invocation) throws Exception {
//		String method = "intercept";
		// 创建InputObject对象
		createInputObject();
		return invocation.invoke();
	}

	private void createInputObject() throws Exception {
		String method = "createInputObject";
		HttpServletRequest request = ServletActionContext.getRequest();
		String path = request.getRequestURI();

		// 处理请求的URI
		if (path.endsWith(".action")) {
			path = path.substring(0, path.indexOf(".action"));
		}

		if (path.startsWith(request.getContextPath())) {
			path = path.substring(request.getContextPath().length(),
					path.length());
		}
		
		// 根据请求的路径取得配置的Action
		Action action = ControlRequestUtil.getActionByPath(path);

		// 获取特定的请求参数UID
		String uid = request.getParameter(ControlConstants.UID);
		if (StringUtil.isEmpty(uid)) {
			uid = null;
		}

		
		// 根据请求组装InputObject对象
		InputObject inputObject = new InputObject();
		try {
			String ip = InetAddress.getLocalHost().getHostAddress().toString(); // 获取服务器IP地址
			inputObject.setServerIp(ip);
		} catch (Exception e) {
			logger.error(method, "Exception Occured When Try To Get IP Address !", e);
		}

		if (action != null) {
			Input input = ControlRequestUtil.getInputByUID(uid, action);
			processRequestParamters(request, inputObject, input);

			if (input != null) {
				if (StringUtil.isNotEmpty(input.getCode())) {
					inputObject.getParams().put("busiCode", input.getCode());
				}

				if (StringUtil.isNotEmpty(input.getService())) {
					inputObject.setService(input.getService());
				} else {
					inputObject.setService(action.getService());
				}

				if (StringUtil.isNotEmpty(input.getMethod())) {
					inputObject.setMethod(input.getMethod());
				} else {
					inputObject.setMethod(action.getMethod());
				}
			}
			ServletActionContext.getContext().put(ControlConstants.OUTPUT, ControlRequestUtil.getOutputByUID(uid, action));
		}

		inputObject.getLogParams().put("VISIT_URL", request.getRequestURL().toString());
		inputObject.getLogParams().put("OP_PATH", path);
		inputObject.getLogParams().put("OP_REQST_NO", UUID.randomUUID().toString());
		ServletActionContext.getContext().put(ControlConstants.INPUTOBJECT, inputObject);
	}

	/**
	 * 处理参数
	 */
	private void processRequestParamters(
			HttpServletRequest request, InputObject inputObject, Input input) {
		List<Parameter> parameters = this.getParameters(request, input);
		StringBuffer cacheKey = new StringBuffer();
		for (Parameter p : parameters) {
			Object object = null;
			String key = p.getKey();
			String toKey = p.getToKey();
			String scope = p.getScope();
			if (StringUtil.isEmpty(key)) {
				continue;
			}
			try {
				String[] values = request.getParameterValues(key);
				if (values == null || values.length == 1) {
					object = request.getParameter(key);
					inputObject.addParams(key, toKey, object == null? null : String.valueOf(object));//单行数据封装到Map<String,String> param
					cacheKey.append(key).append(String.valueOf(object));
					inputObject.addBeans(key, toKey, 0, object == null? null : String.valueOf(object));
				} else {// 多行数据提交
					for (int i = 0; i < values.length; i++) {
						inputObject.addBeans(key, toKey, i, values[i]);
					}
				}
				if (input.isCache()){
					inputObject.addParams(ControlConstants.CACHE_KEY, null, cacheKey.toString());
				}
			} catch (Exception e) {
				logger.error("processRequestParamters", "", e);
			}
		}
	}
	
	private List<Parameter> getParameters(HttpServletRequest request, Input input) {
		List<Parameter> params = new ArrayList<Parameter>();
		if (ControlConstants.INPUT_SCOPE.REQUEST.equals(input.getScope())) {// 从request中取key
			Enumeration<String> enumeration = request.getParameterNames();
			while (enumeration.hasMoreElements()) {
				String key = enumeration.nextElement();
//				if (!ControlConstants.UID.equals(key))
				params.add(new Parameter(key, input.getToKeyByKey(key)));
			}
			for (Parameter param : input.getParameters()) {
				if (param.getScope() != null && !ControlConstants.PARAM_SCOPE.REQUEST.equals(param.getScope())){
					params.add(param);
				}
			}
		} else if (input.getScope() == null || ControlConstants.INPUT_SCOPE.CONTROL.equals(input.getScope())) {
			params.add(new Parameter(ControlConstants.UID, null));
			params.addAll(input.getParameters());
		}
		return params;
	}
}