package com.test.testnetty.controller;

import com.test.testnetty.handler.SendHandler;
import com.test.testnetty.utils.CommonUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 发送消息
 *
 * @author zhaoy
 * @date 2023-09-13 16:07:40
 */
@RestController
@RequestMapping("/test")
public class SendMsgController {

    @GetMapping("/sendToUser")
    public String sendToUser(@RequestParam String toUser, @RequestParam String msg) {
        SendHandler.sendToClient(toUser, CommonUtil.websocketMessage("1", msg, toUser));
        return "ok";
    }

    @GetMapping("/sendToGroup")
    public String sendToGroup(@RequestParam String groupId, @RequestParam String msg) {
        SendHandler.sendToGroup(groupId, CommonUtil.websocketMessage("1", msg, "0"));
        return "ok";
    }

    @GetMapping("/sendToBroadcast")
    public String sendToBroadcast(@RequestParam String msg) {
        SendHandler.broadcastMessage(CommonUtil.websocketMessage("1", msg, "0"));
        return "ok";
    }
}
