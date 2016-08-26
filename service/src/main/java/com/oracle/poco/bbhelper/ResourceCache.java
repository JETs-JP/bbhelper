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

    @Autowired
    private BbhelperLogger logger;

    private Map<FloorCategory, List<Resource>> cacheByFloor = 
            new HashMap<FloorCategory, List<Resource>>();

    private Map<String, Resource> cacheByResourceId = 
            new HashMap<String, Resource>();

    public ResourceCache() throws IOException {
        loadBookableResources();
    }

    private void loadBookableResources() throws IOException {
        try {
            for (FloorCategory category : FloorCategory.values()) {
                InputStream in = this.getClass().getClassLoader().
                    getResourceAsStream(category.getJsonResource());
                List<Resource> list = new ObjectMapper().
                        readValue(in, new TypeReference<List<Resource>>(){});
                cacheByFloor.put(category, list);
                for (Resource r : list) {
                    cacheByResourceId.put(r.getResource_id(), r);
                }
            }
        } catch (IOException e) {
            logger.severe(ErrorDescription.FAILED_TO_LOAD_RESOURCE);
            throw e;
        }
    }

    Map<String, Resource> getCache() {
        return cacheByResourceId;
    }

    Map<String, Resource> getCache(FloorCategory floorCategory) {
        if (floorCategory == null) {
            return getCache();
        }
        List<Resource> resources = cacheByFloor.get(floorCategory);
        Map<String, Resource> retval =
                new HashMap<String, Resource>(resources.size());
        resources.stream().forEach(r -> {
            retval.put(r.getResource_id(), r);
        });
        return retval;
    }

    Resource getResource(String resource_id) {
        // null check不要。reource_idがnullならnullを返す
        return cacheByResourceId.get(resource_id);
    }

    List<String> getCalendarIds() {
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
        List<Resource> list = cacheByFloor.get(floorCategory);
        List<String> retval = new ArrayList<String>(list.size());
        list.stream().forEach(r -> {
            retval.add(r.getCalendar_id());}
        );
        return retval;
    }

}
