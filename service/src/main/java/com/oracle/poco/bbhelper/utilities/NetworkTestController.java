package com.oracle.poco.bbhelper.utilities;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/network")
public class NetworkTestController {

    private static String TIMEOUT = "30000";

    @RequestMapping(value = "/ping",
                    method = RequestMethod.GET)
    public static boolean ping(String target)
            throws InterruptedException, IOException {
        // TODO: エラーハンドリング
        InetAddress byName = InetAddress.getByName(target);
        // Windows の場合
//        String[] command = {
//                 "ping", "-n", "1", "-w", TIMEOUT, byName.getHostAddress()};
        // Linux の場合
        String[] command = {
                 "ping", "-c", "1", "-t", TIMEOUT, byName.getHostAddress()};
        return new ProcessBuilder(command).start().waitFor() == 0;
    }

    @RequestMapping(value = "/isReachable",
                    method = RequestMethod.GET)
    public static boolean isReachable(String target)
            throws UnknownHostException, IOException {
        // TODO: エラーハンドリング
        return InetAddress.getByName(target).isReachable(1000);
    }

}
