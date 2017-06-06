package com.oracle.poco.bbhelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.poco.bbhelper.exception.ErrorDescription;
import com.oracle.poco.bbhelper.log.BbhelperLogger;
import com.oracle.poco.bbhelper.model.Resource;

@Component
class ResourceCache {

    /**
     * 会議室のResourceIdをキーに、会議室情報を保持するキャッシュ
     */
    // TODO ResourceId オブジェクトを定義する。キーを型で縛る
    private final Map<String, Resource> cacheByResourceId;
    /**
     * 会議室の用途分類をキーに、会議室情報のリストを保持するキャッシュ
     */
    // TODO ResourceId オブジェクトを定義する。キーを型で縛る
    private final Map<FloorCategory, Map<String, Resource>> cacheByFloorAndResourceId;

    /**
     * コンストラクタ
     *
     * @throws IOException 会議室情報が記述されたファイルへのアクセスでエラーが発生した場合
     */
    @Autowired
    ResourceCache(BbhelperLogger logger) throws IOException {
        if (logger == null) {
            throw new IllegalArgumentException("Logger is not assigned.");
        }
        try {
            Map<FloorCategory, Map<String, Resource>> tempMapByFloorAndResourceId = new HashMap<>();
            Map<String, Resource> tempMapByResourceId = new HashMap<>();
            for (FloorCategory category : FloorCategory.values()) {
                InputStream in = this.getClass().getClassLoader().getResourceAsStream(
                        category.getJsonResource());
                List<Resource> list =
                        new ObjectMapper().readValue(in, new TypeReference<List<Resource>>(){});
                Map<String, Resource> map = new HashMap<>();
                for (Resource r : list) {
                    map.put(r.getResourceId(), r);
                }
                tempMapByFloorAndResourceId.put(category, Collections.unmodifiableMap(map));
                tempMapByResourceId.putAll(map);
            }
            cacheByFloorAndResourceId = Collections.unmodifiableMap(tempMapByFloorAndResourceId);
            cacheByResourceId = Collections.unmodifiableMap(tempMapByResourceId);
        } catch (IOException e) {
            logger.severe(ErrorDescription.FAILED_TO_LOAD_RESOURCE);
            throw e;
        }
    }

    /**
     * 会議室情報を保持するマップを返却する
     *
     * @return 会議室情報を保持するマップ
     */
    Map<String, Resource> getCache() {
        return cacheByResourceId;
    }

    /**
     * 会議室情報を保持するマップを返却する
     *
     * @param floorCategory 会議室の用途分類
     * @return 会議室情報を保持するマップを返却する。
     *         floorCategoryがnullの場合、すべての会議室情報を含むマップを返却する
     */
    Map<String, Resource> getCache(FloorCategory floorCategory) {
        if (floorCategory == null) {
            return getCache();
        }
        return cacheByFloorAndResourceId.get(floorCategory);
    }

    /**
     * 指定したリソースIDの会議室オブジェクトを返却する
     *
     * @param resource_id 会議室のリソースID。BeehiveのAPIで定義されているもの
     * @return 会議室情報を表現するオブジェクト。resource_idがnullの場合はnull
     */
    Resource getResource(String resource_id) {
        // null check不要。resource_idがnullならnullを返す
        return cacheByResourceId.get(resource_id);
    }

    /**
     * 会議室のカレンダーIDのリストを返却する
     *
     * @param floorCategory 会議室の用途分類
     * @return 会議室のカレンダーIDのリストを返却する
     *         floorCategoryがnullの場合、すべての会議室のカレンダーIDを返却する
     */
    List<String> getCalendarIds(FloorCategory floorCategory) {
        Map<String, Resource> map = this.getCache(floorCategory);
        List<String> resourceIds = new ArrayList<>(map.size());
        map.values().forEach(r -> resourceIds.add(r.getCalendarId()));
        return Collections.unmodifiableList(resourceIds);
    }

}
