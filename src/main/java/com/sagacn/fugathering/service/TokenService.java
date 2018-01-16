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

    public String createToken() {
        String token = UUID.randomUUID().toString();
        return token;
    }

}
