package com.oracle.poco.bbhelper;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/system")
public class SystemOperationController {

    /**
     * シンプルな文字列を返却します。サーバーの稼働を確認する目的で設けられたAPIです。
     * 
     * @return 文字列 "I'm working..."
     */
    //TODO beehiveとの接続もチェックする
    @RequestMapping(value = "/echo",
                    method = RequestMethod.GET)
    public String ping() {
        return "I'm working...";
    }

}
