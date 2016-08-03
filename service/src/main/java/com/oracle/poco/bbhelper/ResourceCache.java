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
                        readValue(in, new TypeReference<List<ResourceWithInvitationsInRange>>(){});
                Map<String, ResourceWithInvitationsInRange> map =
                        new HashMap<String, ResourceWithInvitationsInRange>();
                for (ResourceWithInvitationsInRange r : list) {
                    map.put(r.getResource_id(), r);
                }
                this.cache.put(category, map);
            }
        } catch (IOException e) {
            logger.severe(ErrorDescription.FAILET_TO_LOAD_RESOURCES);
            System.exit(1);
        }
    }

    Collection<ResourceWithInvitationsInRange> getAllResources() {
        List<ResourceWithInvitationsInRange> retval =
                new ArrayList<ResourceWithInvitationsInRange>();
        for (FloorCategory category : FloorCategory.values()) {
            for (ResourceWithInvitationsInRange resource : cache.get(category).values()) {
                retval.add(ResourceWithInvitationsInRange.deepClone(resource));
            }
        }
        return retval;
    }

//    ResourceWithInvitationsInRange get(String resource_id) {
//        // null check不要。reource_idがnullならnullを返す
//        ResourceWithInvitationsInRange origin = cache.get(resource_id);
//        if (origin == null) {
//            return null;
//        }
//        return ResourceWithInvitationsInRange.deepClone(origin);
//    }

      // TODO キャシュするようにしてパフォーマンスを改善する
//    Set<String> getAllCalendarIds () {
//        Set<String> retval = new HashSet<String>(cache.size());
//        cache.values().stream().parallel().forEach(r -> {
//            retval.add(r.getCalendar_id());}
//        );
//        return retval;
//    }
//
    // TODO キャシュするようにしてパフォーマンスを改善する
    Set<String> getCalendarIds(FloorCategory floorCategory) {
        Map<String, ResourceWithInvitationsInRange> map = cache.get(floorCategory);
        Set<String> retval = new HashSet<String>(map.size());
        map.values().stream().parallel().forEach(r -> {
            retval.add(r.getCalendar_id());}
        );
        return retval;
    }

}
