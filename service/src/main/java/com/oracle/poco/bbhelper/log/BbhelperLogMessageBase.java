package com.oracle.poco.bbhelper.log;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * 本アプリのログメッセージの継承元となる抽象クラス<br>
 *
 * ログメッセージオブジェクトを定義するには、このクラスを継承し、
 * このクラスを含むパッケージからアクセス可能なデフォルトコンストラクタを定義します。
 *
 * Created by hhayakaw on 2017/06/19.
 */
abstract class BbhelperLogMessageBase implements BbhelperLogMessage {

    @Override
    @JsonIgnore
    public final Marker getMarker() {
        return MarkerFactory.getMarker(this.getClass().getName());
    }

}
