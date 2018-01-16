package com.sagacn.fugathering.web.handler.impl;

import com.sagacn.fugathering.consts.CacheKeyConstant;
import com.sagacn.fugathering.dto.ClientRequest;
import com.sagacn.fugathering.exception.BaseException;
import com.sagacn.fugathering.service.RedisService;
import com.sagacn.fugathering.util.ErrorCode;
import com.sagacn.fugathering.web.handler.Handler;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 */
@Service("handler_1001-1")
public class H1001V1CheckReward implements Handler {


	@Autowired
	private RedisService redis;

	@Override
	public Object process(ClientRequest clientRequest) throws Exception {
		String token = clientRequest.getToken();
		String userToken = clientRequest.getParameter("userToken");
		if (token!=null&&token.equals(userToken)){
			throw new BaseException(ErrorCode.NOT_ADMIN);
		}
		if (userToken==null){
			throw new BaseException(ErrorCode.INVALID_TOKEN);
		}
		if(redis.hexists(CacheKeyConstant.DRAWN_TIME_MAP_KEY,userToken)){
			throw new BaseException(ErrorCode.REWARD_HAS_SENT);
		}
		redis.hset(CacheKeyConstant.DRAWN_TIME_MAP_KEY,userToken, System.currentTimeMillis()+"");
		return null;
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
