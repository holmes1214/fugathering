package com.sagacn.fugathering.web.handler.impl;

import com.sagacn.fugathering.consts.CacheKeyConstant;
import com.sagacn.fugathering.dto.ClientRequest;
import com.sagacn.fugathering.exception.BaseException;
import com.sagacn.fugathering.service.RedisService;
import com.sagacn.fugathering.service.TokenService;
import com.sagacn.fugathering.util.ErrorCode;
import com.sagacn.fugathering.web.handler.Handler;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 */
@Service("handler_1002-1")
public class H1002V1CreateToken implements Handler {


	@Autowired
	private TokenService service;

	@Value("${fu.picture.count}")
	private int picCount;
	@Value("${fu.picture.qrCount}")
	private int qrCount;

	@Override
	public Object process(ClientRequest clientRequest) throws Exception {
		return service.createToken();
	}

	@Override
	public boolean checkToken() {
		return false;
	}

	@Override
	public boolean validateData(ClientRequest clientRequest) {
		return true;
	}
}
