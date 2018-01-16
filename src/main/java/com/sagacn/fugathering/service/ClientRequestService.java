package com.sagacn.fugathering.service;

import com.sagacn.fugathering.consts.ActionCode;
import com.sagacn.fugathering.dto.ClientRequest;
import com.sagacn.fugathering.dto.Success;
import com.sagacn.fugathering.exception.BaseException;
import com.sagacn.fugathering.util.ErrorCode;
import com.sagacn.fugathering.web.handler.Handler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Service
public class ClientRequestService {

	private static final String HANDLER_PREFIX="handler_";
	
	private Logger logger = LoggerFactory.getLogger(ClientRequestService.class);

	@Autowired
	private Map<String, Handler> handlers;
	public Success process(String body, HttpServletRequest request) {
		long start = System.currentTimeMillis();
		String sn = "unknow";
		ErrorCode errorcode = ErrorCode.OK;
		try{
			if(StringUtils.isEmpty(body)) {
				return new Success(sn, ErrorCode.PARAMETER_ERROR.value, "body is null");
			}
			ClientRequest clientRequest = ClientRequest.fromJsonToClientRequest(body);
			clientRequest.setIp(request.getHeader("X-Real-IP"));
			String method = clientRequest.getMethod();
			String methodMemo = ActionCode.getMethodMemo(method);
			logger.debug("sn:{}, method:{}, memo:{}", sn, method, methodMemo);
			Handler handler = handlers.get(HANDLER_PREFIX+method);
			String userid = null;
			if (null != handler) {
				try {
					logger.info("请求记录  method:{} methodMemo:{} token:{}", new Object[] {method, methodMemo, clientRequest.getToken()});
					return new Success(sn, errorcode.value, handler.process(clientRequest));
				} catch (BaseException e) {
					errorcode = null == e.getErrorCode() ? ErrorCode.SystemError : e.getErrorCode();
					logger.error("handler请求出错,token:" + clientRequest.getToken() + ",sn:"+sn+", errorcode:"+errorcode.value+", errormsg:"+e.getMessage());
				} catch (Exception e) {
					errorcode = ErrorCode.SystemError;
					logger.error("handler请求出错,error token:" + clientRequest.getToken() + " sn:" + sn, e);
				} finally {
					logger.info("sn:{}, method:{}, memo:{}, executeTime:{}", new Object[] {sn, method, methodMemo, System.currentTimeMillis() - start});
				}
			} else {
				errorcode = ErrorCode.NO_METHOD;
			}
		} catch(Throwable e) {
			logger.error("process error, body" + body,e);
			errorcode = ErrorCode.SystemError;
		}
		return new Success(sn, errorcode.value, errorcode.memo);
	}
}
