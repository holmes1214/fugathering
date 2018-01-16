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
        if (StringUtils.isBlank(token)) {
            throw new BaseException(ErrorCode.INVALID_TOKEN);
        }
        String qrNumber = clientRequest.getParameter("qrNumber");
        String qrKey = CacheKeyConstant.QR_DRAWN_PREFIX + token;
        if (redis.exists(qrKey) && redis.sismember(qrKey, qrNumber)) {
            throw new BaseException(ErrorCode.DUPLICATED_QR_CODE);
        }
        Map<String, Object> result = new HashMap<>();
        int i = 1;
        Double vainCount = redis.zscore(CacheKeyConstant.VAIN_MAP_KEY, token);
        if (vainCount != null && vainCount > 1) {
            i = RandomUtils.nextInt(0, 10);
        }
        Set<String> pic = redis.smembers(CacheKeyConstant.FU_SET_PREFIX + token);
        List<String> total = redis.lrange(CacheKeyConstant.FU_SET_TOTAL, 0, -1);
        redis.sadd(qrKey, qrNumber);
        if (i == 0 || pic.size() >= 5) {
            result.put("luckyDraw", 0);
            redis.zincreby(CacheKeyConstant.VAIN_MAP_KEY, token, 1);
        } else {
            total.removeAll(pic);
            String index = total.get(RandomUtils.nextInt(0, total.size()));
            result.put("luckyDraw", index);
            redis.sadd(CacheKeyConstant.FU_SET_PREFIX + token, index);
            pic.add(index);
        }
        result.put("fuLists", pic);
        return result;
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
