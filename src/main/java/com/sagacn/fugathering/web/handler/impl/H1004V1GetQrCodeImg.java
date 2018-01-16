package com.sagacn.fugathering.web.handler.impl;

import com.sagacn.fugathering.consts.CacheKeyConstant;
import com.sagacn.fugathering.dto.ClientRequest;
import com.sagacn.fugathering.exception.BaseException;
import com.sagacn.fugathering.service.RedisService;
import com.sagacn.fugathering.util.ErrorCode;
import com.sagacn.fugathering.util.JsonUtil;
import com.sagacn.fugathering.web.handler.Handler;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 */
@Service("handler_1004-1")
public class H1004V1GetQrCodeImg implements Handler {


    private Logger logger = LoggerFactory.getLogger(H1004V1GetQrCodeImg.class);
    @Autowired
    private RedisService redis;

    @Override
    public Object process(ClientRequest clientRequest) throws Exception {
        String token = clientRequest.getToken();
        if (StringUtils.isBlank(token)) {
            throw new BaseException(ErrorCode.INVALID_TOKEN);
        }
        return getQrPic(token);
    }

    private String getQrPic(String token) {
        String key = CacheKeyConstant.DRAWN_TIME_MAP_KEY;
        if (redis.hexists(key, token)) {
            return redis.hget(key, token);
        }
        String url = "http://evtape.cn/qrimage/" + token + ".png";
        createQRPicture(token);
        redis.hset(key, token, url);
        return url;
    }

    private void createQRPicture(String token) {
        File image = new File("/down/qrcode/" + token + ".png");
        try {
            String url="http://evtape.cn/reward.html?token="+token;
            JsonUtil.drawLogoQRCode(image, url, null);
        } catch (Exception e) {
            logger.error("create qr code error: ", e);
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
