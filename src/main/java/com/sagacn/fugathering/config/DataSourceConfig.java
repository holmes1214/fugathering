/*
 * Copyright (c) 2016. 版权所有,归北京易精灵科技有限公司.
 */

package com.sagacn.fugathering.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * DataSourceConfiguration
 *
 * @author Eric
 * @date 16/4/28
 */
@Configuration
public class DataSourceConfig {

    @Value("${aliyun.redis.max-idle}")
    private int redisPoolMaxIdle;
    @Value("${aliyun.redis.min-idle}")
    private int redisPoolMinIdle;
    @Value("${aliyun.redis.max-active}")
    private int redisPoolMaxActive;
    @Value("${aliyun.redis.max-wait}")
    private int redisPoolMaxWait;

    @Value("${aliyun.redis.host}")
    private String redisHost;
    @Value("${aliyun.redis.password}")
    private String password;
    @Value("${aliyun.redis.port}")
    private int redisPort;
    @Value("${aliyun.redis.timeout}")
    private  int redisTimeout;

    @Bean
    public JedisPool jedisPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(redisPoolMaxIdle);
        config.setMaxTotal(redisPoolMaxActive);
        config.setMinIdle(redisPoolMinIdle);
        config.setMaxWaitMillis(redisPoolMaxWait);
        config.setTestOnBorrow(false);
        config.setTestOnReturn(false);
        JedisPool pool = new JedisPool(config, redisHost, redisPort, redisTimeout, password);
        return pool;
    }
}
