package com.sagacn.fugathering.consts;

import java.util.HashMap;
import java.util.Map;

public enum ActionCode {




	RequestSMS("1000-1","发送验证码"),
	;
	
	private String code;
	
	private String memo;
	
	private static Map<String, String> map = new HashMap<String, String>();
	
	static {
		ActionCode[] codes = values();
		for(ActionCode code : codes) {
			map.put(code.getCode(), code.getMemo());
		}
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	private ActionCode(String code, String memo) {
		this.code = code;
		this.memo = memo;
	}
	
	public static String getMethodMemo(String method) {
		if(!map.containsKey(method)) {
			return "Unknown Method";
		}
		return map.get(method);
	}
}
