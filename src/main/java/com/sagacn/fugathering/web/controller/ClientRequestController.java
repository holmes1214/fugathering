package com.sagacn.fugathering.web.controller;

import com.sagacn.fugathering.dto.Success;
import com.sagacn.fugathering.service.ClientRequestService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/qr")
public class ClientRequestController {
	
	private static Logger logger = LoggerFactory.getLogger(ClientRequestController.class);

	@Autowired
	private ClientRequestService requestService;

	@RequestMapping(value = "/request", method = RequestMethod.POST)
	public void request(@RequestParam("body") String body, HttpServletRequest request, HttpServletResponse response) throws Exception {
		long start = System.currentTimeMillis();
		logger.info("客户端请求body：{}", body);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		Success success = requestService.process(body, request);
		String json = success.toJson();
		long end = System.currentTimeMillis();
		logger.debug("客户端请求返回,executeTime:{},json:{}", new Object[] {end - start, json});
		response.getWriter().write(json);
		response.getWriter().flush();
		response.getWriter().close();
	}
	
}
