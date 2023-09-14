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
}
