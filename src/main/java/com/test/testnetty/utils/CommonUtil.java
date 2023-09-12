package com.test.testnetty.utils;

import com.alibaba.fastjson2.JSONObject;
import com.test.testnetty.entity.WebsocketMessage;

/**
 * 通用工具
 *
 * @author tempest
 * @date 2023-09-12 15:05:38
 */
public class CommonUtil {

    /**
     * 组装websocket信息
     *
     * @param code   登陆人code 服务端则为0
     * @param msg    消息
     * @param toCode 接收人code
     * @return jsonString
     */
    public static String websocketMessage(String code, String msg, String toCode) {
        return JSONObject.toJSONString(new WebsocketMessage(code, msg, toCode));
    }
}
