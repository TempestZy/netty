package com.test.testnetty.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 通信实体
 *
 * @author tempest
 * @date 2023-09-12 10:53:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebsocketMessage implements Serializable {

    /**
     * 当前客户端编号
     */
    private String code;

    /**
     * 信息
     */
    private String msg;

    /**
     * 接收客户端编号
     */
    private String toCode;

    /**
     * 组id
     */
    private String groupId;

    public WebsocketMessage(String code) {
        this.code = code;
    }

    public WebsocketMessage(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public WebsocketMessage(String code, String msg, String toCode) {
        this.code = code;
        this.msg = msg;
        this.toCode = toCode;
    }
}
