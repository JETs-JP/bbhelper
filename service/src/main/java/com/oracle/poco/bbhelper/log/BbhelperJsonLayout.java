package com.oracle.poco.bbhelper.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.contrib.json.classic.JsonLayout;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.poco.bbhelper.exception.BbhelperRuntimeException;
import org.slf4j.Marker;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * ログをJSON形式で保存するためのLayoutオブジェクト<br>
 *
 * メッセージとしてログメッセージオブジェクト(BbhelperLogMessage)を指定してすると、
 * レコードに含まれるJSONとしてそのメッセージを出力します。
 */
public class BbhelperJsonLayout extends JsonLayout {

    private static final BbhelperLogger logger = BbhelperLogger.getLogger(BbhelperJsonLayout.class);

    private ObjectMapper objectMapper;

    private Map<String, Class<? extends BbhelperLogMessage>> messageClassMap = new HashMap<>();

    public BbhelperJsonLayout() {
        super();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected final void addCustomDataToJsonMap(Map<String, Object> map, ILoggingEvent event) {
        addBbhelperMessage(FORMATTED_MESSAGE_ATTR_NAME, this.includeFormattedMessage, event,
                event.getFormattedMessage(), map);
        addBbhelperMessage(MESSAGE_ATTR_NAME, this.includeMessage, event, event.getMessage(), map);
    }

    private void addBbhelperMessage(String key, boolean field, ILoggingEvent event,
                                    String message, Map<String, Object> map) {
        if (message == null) {
            return;
        }
        Marker marker = event.getMarker();
        if (marker == null) {
            add(key, field, message, map);
            return;
        }
        Class<? extends BbhelperLogMessage> clazz = resolveMessageClass(marker);
        if (clazz == null) {
            add(key, field, message, map);
            return;
        }
        try {
            /*
             * MessageオブジェクトのJSONの文字列がStringとして渡ってくるので、
             * 一旦Deserializeした上で更にMapに変換しmapを更新する
             */
            BbhelperLogMessage msg = objectMapper.readValue(message, clazz);
            addMap(key, field, msg.toMap(), map);
        } catch (IOException e) {
            /*
             * Mssageオブジェクトに変換できなかった場合は、Stringとしてそのままログ出力する
             */
            add(key, field, message, map);
            logger.warn("Failed to log a BbhelperLogMessage as a JSON object.");
        }
    }

    private Class<? extends BbhelperLogMessage> resolveMessageClass(Marker marker) {
        Marker candidate = null;
        if (this.messageClassMap.keySet().contains(marker.getName())) {
            candidate = marker;
        }
        Iterator<Marker> itr = marker.iterator();
        while (itr.hasNext()) {
            Marker m = itr.next();
            if (this.messageClassMap.keySet().contains(m.getName())) {
                if (candidate != null) {
                    logger.warn("Failed to log a BbhelperLogMessage as a JSON object.");
                    return null;
                } else {
                    candidate = m;
                }
            }
        }
        if (candidate == null) {
            return null;
        }
        return this.messageClassMap.get(candidate.getName());
    }

    public void addMessageClass(String messageClass) {
        try {
            Object obj = Class.forName(messageClass).newInstance();
            Class<? extends BbhelperLogMessage> clazz = ((BbhelperLogMessage) obj).getClass();
            this.messageClassMap.put(messageClass, clazz);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new BbhelperRuntimeException("Failed to load logback-spring.xml property", e);
        }
    }

}

