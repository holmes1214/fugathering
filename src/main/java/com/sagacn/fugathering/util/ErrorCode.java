package com.sagacn.fugathering.util;

import java.util.HashMap;
import java.util.Map;

public enum ErrorCode {

    OK("0", "成功"),
    INVALID_TOKEN("1000", "用户登录已过期"),
    ADMIN_REQUIRED("1002", "需要管理员"),
    SystemError("9999", "服务器错误"),
    PARAMETER_ERROR("9001", "参数错误"),
    NO_METHOD("9002", "无对应method"),
    RedisError("9008", "Redis错误"),
    FileNotExist("9009", "文件不存在"),
    UserForbidden("100001", "用户不可用"),
    QRCodeGenerateError("100002", "二维码生成错误"),
    TOO_FAST_SMS_REQUEST("100003", "申请短信时间间隔太短"),
    VALIDATION_EXPIRED("100004", "验证码已过期"),
    WRONG_VALIDATION_CODE("100005", "验证码不正确"),
    ReachUrlTotalLimit("100006", "已达到设置的URL最大数目"),
    UPLOAD_ERROR("100007", "上传文件失败"),
    URL_USER_NOT_MATCH("100008", "设置的URL与用户信息不 符"),
    FETCH_VIDEO_GET_PID_FAILURE("100009", "抓取视频进程启动，获取PID失败"),

    IS_RECORDING("100010", "主播正在录制中"),
    SMS_SEND_ERROR("100011", "短信发送失败"),
    URL_ALREADY_EXIST("100011", "URL已存在"),
    FILE_TRANSFORM_ERROR("100012", "文件转换错误"),
    DUPLICATED_QR_CODE("100013", "二维码已经扫过"),
    TRY_AGAIN("100014", "请再接再厉"),
    NOD_ADMIN("100015", "请联系工作人员领取奖品"),
    REWARD_HAS_SENT("100016","奖品已领取过" );

    public String value;

    public String memo;

    private ErrorCode(String value, String memo) {
        this.value = value;
        this.memo = memo;
    }

    private static final Map<String, String> lookup = new HashMap<String, String>();
    private static final Map<String, ErrorCode> lookupErrorCode = new HashMap<String, ErrorCode>();

    static {
        for (ErrorCode code : values()) {
            lookup.put(code.value, code.memo);
            lookupErrorCode.put(code.value, code);
        }
    }

    public static String get(String value) {
        return lookup.get(value);
    }

    public static ErrorCode getErrorCode(String value) {
        return lookupErrorCode.get(value);
    }

    public static Map<String, String> getAllMap() {
        return lookup;
    }
}
