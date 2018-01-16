package com.sagacn.fugathering.service;

import com.sagacn.fugathering.consts.CacheKeyConstant;
import com.sagacn.fugathering.exception.BaseException;
import com.sagacn.fugathering.util.ErrorCode;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by holmes1214 on 30/07/2017.
 */
@Service
public class TokenService {

    @Value("${fu.picture.count}")
    private int picCount;
    @Autowired
    private RedisService redis;

    public String createToken() {
        String token = UUID.randomUUID().toString();
        return token;
    }

    public String getNextPicture(String token,String qrNumber){
        if(redis.sismember(CacheKeyConstant.QR_PREFIX+token,qrNumber)){
            throw new BaseException(ErrorCode.URL_ALREADY_EXIST);
        }
        List<String> total = redis.lrange(CacheKeyConstant.TOTAL_SET, 0, -1);
        Set<String> userPics = redis.smembers(token);
        if (userPics!=null){
            total.removeAll(userPics);
        }
        int i = RandomUtils.nextInt(0, total.size());
        redis.sadd(token,total.get(i));
        redis.sadd(CacheKeyConstant.QR_PREFIX+token,qrNumber);
        return total.get(i);
    }

    public List<String> getUserPictures(String token){
        if (!redis.exists(token)){
            return new ArrayList<>();
        }
        Set<String> userPics = redis.smembers(token);
        List<String> result=new ArrayList<>(userPics);
        Collections.sort(result);
        return result;
    }

    public Long hasDrawn(String token){
        Double zscore = redis.zscore(CacheKeyConstant.DRAWN_TIME_KEY,token);
        if (zscore==null){
            redis.zadd(CacheKeyConstant.DRAWN_TIME_KEY, new Double(System.currentTimeMillis()),token);
            return null;
        }
        return zscore.longValue();
    }
}
