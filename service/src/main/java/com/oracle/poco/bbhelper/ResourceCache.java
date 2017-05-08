package com.oracle.poco.bbhelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * ロガー
     */
    // TODO コンストラクタインジェクションに切り替えて、final宣言を追加する
    @Autowired
    private BbhelperLogger logger;
    /**
     * 会議室の用途分類をキーに、会議室情報のリストを保持するキャッシュ
     */
    private Map<FloorCategory, List<Resource>> cacheByFloor = new HashMap<>();
    /**
     * 会議室のResourceIdをキーに、会議室情報を保持するキャッシュ
     */
    // TODO ResourceId オブジェクトを定義する。キーを型で縛る
    private Map<String, Resource> cacheByResourceId = new HashMap<>();

    /**
     * コンストラクタ
     *
     * @throws IOException 会議室情報が記述されたファイルへのアクセスでエラーが発生した場合
     */
    public ResourceCache() throws IOException {
        try {
            for (FloorCategory category : FloorCategory.values()) {
                InputStream in = this.getClass().getClassLoader().
                        getResourceAsStream(category.getJsonResource());
                List<Resource> list = new ObjectMapper().
                        readValue(in, new TypeReference<List<Resource>>(){});
                cacheByFloor.put(category, list);
                for (Resource r : list) {
                    cacheByResourceId.put(r.getResourceId(), r);
                }
            }
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
    // TODO immutableなオブジェクトを返す
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
    // TODO immutableなオブジェクトを返す
    Map<String, Resource> getCache(FloorCategory floorCategory) {
        if (floorCategory == null) {
            return getCache();
        }
        List<Resource> resources = cacheByFloor.get(floorCategory);
        Map<String, Resource> retval = new HashMap<>(resources.size());
        resources.forEach(r -> retval.put(r.getResourceId(), r));
        return retval;
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
        // TODO floorCategoryがnullのときは、すべての会議室のカレンダーIDを返す
        if (floorCategory == null) {
            floorCategory = FloorCategory.getDefault();
        }
        List<Resource> list = cacheByFloor.get(floorCategory);
        List<String> retval = new ArrayList<>(list.size());
        list.stream().forEach(r -> retval.add(r.getCalendarId()));
        return retval;
    }
}
