package com.oracle.poco.bbhelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.poco.bbhelper.exception.ErrorDescription;
import com.oracle.poco.bbhelper.log.BbhelperLogger;
import com.oracle.poco.bbhelper.model.ResourceWithInvitations;

class ResourceCache {

    @Autowired
    private BbhelperLogger logger;

    private Map<FloorCategory, List<ResourceWithInvitations>> cacheByFloor = 
            new HashMap<FloorCategory, List<ResourceWithInvitations>>();

    private Map<String, ResourceWithInvitations> cacheByResourceId = 
            new HashMap<String, ResourceWithInvitations>();

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
                List<ResourceWithInvitations> list = new ObjectMapper().
                        readValue(in, new TypeReference<List<ResourceWithInvitations>>(){});
                cacheByFloor.put(category, list);
                for (ResourceWithInvitations r : list) {
                    cacheByResourceId.put(r.getResource_id(), r);
                }
            }
        } catch (IOException e) {
            logger.severe(ErrorDescription.FAILET_TO_LOAD_RESOURCES);
            System.exit(1);
        }
    }

    Map<String, ResourceWithInvitations> getClonedCache() {
        Map<String, ResourceWithInvitations> retval =
                new HashMap<String, ResourceWithInvitations>(cacheByResourceId.size());
        cacheByResourceId.entrySet().stream().forEach(e -> {
            ResourceWithInvitations clone = 
                    ResourceWithInvitations.deepClone(e.getValue());
            retval.put(clone.getResource_id(), clone);
        });
        return retval;
    }

    ResourceWithInvitations get(String resource_id) {
        // null check不要。reource_idがnullならnullを返す
        ResourceWithInvitations origin = cacheByResourceId.get(resource_id);
        if (origin == null) {
            return null;
        }
        return ResourceWithInvitations.deepClone(origin);
    }

    List<String> getAllCalendarIds () {
        List<String> retval = new ArrayList<String>(cacheByResourceId.size());
        cacheByResourceId.values().stream().forEach(r -> {
            retval.add(r.getCalendar_id());}
        );
        return retval;
    }

    List<String> getCalendarIds(FloorCategory floorCategory) {
        if (floorCategory == null) {
            floorCategory = FloorCategory.getDafault();
        }
        List<ResourceWithInvitations> list = cacheByFloor.get(floorCategory);
        List<String> retval = new ArrayList<String>(list.size());
        list.stream().forEach(r -> {
            retval.add(r.getCalendar_id());}
        );
        return retval;
    }

}
