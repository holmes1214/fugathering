package com.sagacn.fugathering.web.handler.impl;

import com.google.zxing.WriterException;
import com.sagacn.fugathering.consts.CacheKeyConstant;
import com.sagacn.fugathering.dto.ClientRequest;
import com.sagacn.fugathering.exception.BaseException;
import com.sagacn.fugathering.service.ClientRequestService;
import com.sagacn.fugathering.service.RedisService;
import com.sagacn.fugathering.util.ErrorCode;
import com.sagacn.fugathering.util.JsonUtil;
import com.sagacn.fugathering.web.handler.Handler;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 */
@Service("handler_1000-1")
public class H1000V1GetFuImage implements Handler {


	private Logger logger = LoggerFactory.getLogger(H1000V1GetFuImage.class);
	@Autowired
	private RedisService redis;

	@Override
	public Object process(ClientRequest clientRequest) throws Exception {
		String token = clientRequest.getToken();
		if(StringUtils.isBlank(token)){
			throw new BaseException(ErrorCode.INVALID_TOKEN);
		}
		String qrNumber = clientRequest.getParameter("qrNumber");
		String qrKey= CacheKeyConstant.QR_DRAWN_PREFIX+token;
		if (redis.exists(qrKey)&&redis.sismember(qrKey,qrNumber)){
			throw new BaseException(ErrorCode.DUPLICATED_QR_CODE);
		}
		Map<String,Object> result=new HashMap<>();
		int i = RandomUtils.nextInt(0, 100);
		Set<String> pic = redis.smembers(CacheKeyConstant.FU_SET_PREFIX + token);
		if (i==0){
			result.put("luckyDraw",0);
		}else {
			redis.sadd(qrKey,qrNumber);
			List<String> total = redis.lrange(CacheKeyConstant.FU_SET_TOTAL,0,-1);
			total.removeAll(pic);
			if (total.size()==0){
				result.put("qrPic",getQrPic(token));
			}else {
				String index=total.get(RandomUtils.nextInt(0,total.size()));
				result.put("luckyDraw",index);
				redis.sadd(CacheKeyConstant.FU_SET_PREFIX + token,index);
				pic.add(index);
			}
		}
		result.put("fuLists",pic);
		return result;
	}

	private String getQrPic(String token) {
		String key=CacheKeyConstant.DRAWN_TIME_MAP_KEY;
		if (redis.hexists(key,token)){
			return redis.hget(key,token);
		}
		String url="http://evtape.cn/qrimage/"+token+".png";
		createQRPicture(token,url);
		redis.hset(key,token,url);
		return url;
	}

	private void createQRPicture(String token,String url) {
		File image=new File("/down/uservideo1/"+token+".png");
		try {
			JsonUtil.drawLogoQRCode(image,url,null);
		} catch (Exception e) {
			logger.error("create qr code error: ",e);
			throw new BaseException(ErrorCode.QRCodeGenerateError);
		}
	}


	@Override
	public boolean checkToken() {
		return true;
	}

	@Override
	public boolean validateData(ClientRequest clientRequest) {
		return true;
	}
}
