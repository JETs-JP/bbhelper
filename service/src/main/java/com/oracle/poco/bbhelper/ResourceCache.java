package com.oracle.poco.bbhelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.poco.bbhelper.log.BbhelperLogger;
import com.oracle.poco.bbhelper.model.ResourceWithInvitationsInRange;

class ResourceCache {

    private static final String FILE_PATH_RESOURCES = "resources.json";

    private Map<String, ResourceWithInvitationsInRange> cache =
            new HashMap<String, ResourceWithInvitationsInRange>();

    private static ResourceCache instance = null;

    static ResourceCache getInstance() {
        initialize();
        return instance;
    }

    static void initialize() {
        if (instance == null) {
            instance = new ResourceCache();
            instance.loadBookableResources();
        }
    }

    private void loadBookableResources() {
        try {
            InputStream in = this.getClass().getClassLoader().
                    getResourceAsStream(FILE_PATH_RESOURCES);
            List<ResourceWithInvitationsInRange> list = new ObjectMapper().readValue(
                    in, new TypeReference<List<ResourceWithInvitationsInRange>>() {
            });
            for (ResourceWithInvitationsInRange br : list) {
                cache.put(br.getResource_id(), br);
            }
        } catch (IOException e) {
            BbhelperLogger.getInstance().severe("failed to load bookable resources data.");
            System.exit(1);
        }
    }

    Collection<ResourceWithInvitationsInRange> getAllResources() {
        List<ResourceWithInvitationsInRange> retval = new ArrayList<ResourceWithInvitationsInRange>();
        for (ResourceWithInvitationsInRange resource : cache.values()) {
            retval.add(ResourceWithInvitationsInRange.deepClone(resource));
        }
        return retval;
    }

    ResourceWithInvitationsInRange get(String resource_id) {
        // null check不要。reource_idがnullならnullを返す
        ResourceWithInvitationsInRange origin = cache.get(resource_id);
        if (origin == null) {
            return null;
        }
        return ResourceWithInvitationsInRange.deepClone(origin);
    }

    // TODO キャシュするようにしてパフォーマンスを改善する
    Set<String> getAllCalendarIds () {
        Set<String> retval = new HashSet<String>(cache.size());
        cache.values().stream().parallel().forEach(r -> {
            retval.add(r.getCalendar_id());}
        );
        return retval;
    }

}
