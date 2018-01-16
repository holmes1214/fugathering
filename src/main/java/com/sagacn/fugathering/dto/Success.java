package com.sagacn.fugathering.dto;

import com.alibaba.fastjson.JSON;

public class Success {
	
	private String sn = null;
	
	private String errorcode;
	
	private Object entity = null;

	public Success(String sn) {
		super();
	}

	public Success(String sn , String errorcode, Object entity) {
		super();
		this.sn = sn;
		this.errorcode = errorcode;
		this.entity = entity;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}
	
	public String getErrorcode() {
		return errorcode;
	}

	public void setErrorcode(String errorcode) {
		this.errorcode = errorcode;
	}

	public Object getEntity() {
		return entity;
	}

	public void setEntity(Object entity) {
		this.entity = entity;
	}

	public String toJson() {
		return JSON.toJSONString(this);
	}
}
