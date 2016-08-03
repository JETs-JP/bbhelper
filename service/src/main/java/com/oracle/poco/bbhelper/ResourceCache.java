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

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.poco.bbhelper.exception.ErrorDescription;
import com.oracle.poco.bbhelper.log.BbhelperLogger;
import com.oracle.poco.bbhelper.model.ResourceWithInvitationsInRange;

class ResourceCache {

    @Autowired
    private BbhelperLogger logger;

    private static final String FILE_PATH_RESOURCES_WORK = "resources_work.json";

    private Map<FloorCategory, Map<String, ResourceWithInvitationsInRange>> cache =
            new HashMap<FloorCategory, Map<String, ResourceWithInvitationsInRange>>();

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
            for (FloorCategory category : FloorCategory.values()) {
                InputStream in = this.getClass().getClassLoader().
                    getResourceAsStream(category.getJsonResource());
                List<ResourceWithInvitationsInRange> list = new ObjectMapper().
                        readValue(in, new TypeReference<List<ResourceWithInvitationsInRange>>() {});
                Map<String, ResourceWithInvitationsInRange> cache =
                        new HashMap<String, ResourceWithInvitationsInRange>();
                for (ResourceWithInvitationsInRange br : list) {
                    cache.put(br.getResource_id(), br);
                }
                this.cache.put(category, cache);
            }
        } catch (IOException e) {
            logger.severe(ErrorDescription.FAILET_TO_LOAD_RESOURCES);
            System.exit(1);
        }
    }

    Collection<ResourceWithInvitationsInRange> getAllResources() {
        List<ResourceWithInvitationsInRange> retval =
                new ArrayList<ResourceWithInvitationsInRange>();
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

    Set<String> getCalendarIds(FloorCategory floorCategory) {
        Set<String> retval = new HashSet<String>(cache.size());
        cache.values().stream().parallel().forEach(r -> {
            retval.add(r.getCalendar_id());}
        );
        return retval;
    }

}
