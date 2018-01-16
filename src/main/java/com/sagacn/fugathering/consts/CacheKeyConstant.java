package com.sagacn.fugathering.consts;

/**
 * 定义保存在redis中的所有key<br/>
 * @author ZHAILEI
 *
 */
public interface CacheKeyConstant {
	
    String FU_SET_PREFIX = "fu.gathering.set.";
    String FU_SET_TOTAL = "fu.gathering.total";
    String QR_DRAWN_PREFIX = "fu.gathering.qr.";
    String DRAWN_TIME_MAP_KEY = "fu.gathering.drawn.time" ;
}
