package com.oracle.poco.bbhelper.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.poco.bbhelper.model.Resource;

class ResourceCache {

    private static final String FILE_PATH_RESOURCES = "resources.json";

    private Map<String, Resource> cache =
            new HashMap<String, Resource>();

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
            List<Resource> list = new ObjectMapper().readValue(
                    in, new TypeReference<List<Resource>>() {
            });
            for (Resource br : list) {
                cache.put(br.getResource_id(), br);
            }
        } catch (IOException e) {
            LoggerManager.getLogger().severe(
                    "failed to load bookable resources data.");;
            e.printStackTrace();
            System.exit(1);
        }
    }

    Collection<Resource> getAllResources() {
        List<Resource> retval = new ArrayList<Resource>();
        for (Resource resource : cache.values()) {
            retval.add(Resource.deepClone(resource));
        }
        return retval;
    }

    Resource get(String resource_id) {
        // null check不要。reource_idがnullならnullを返す
        Resource origin = cache.get(resource_id);
        if (origin == null) {
            return null;
        }
        return Resource.deepClone(origin);
    }

}
