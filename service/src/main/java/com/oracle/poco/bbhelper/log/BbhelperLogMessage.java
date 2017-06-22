package com.oracle.poco.bbhelper.log;

import org.slf4j.Marker;

import java.util.Map;

/**
 * 本アプリケーションのログメッセージが実装すべきインターフェース
 *
 * Created by hhayakaw on 2017/06/19.
 */
public interface BbhelperLogMessage {

    Map<String, Object> toMap();

    Marker getMarker();

}
