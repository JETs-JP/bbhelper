package com.oracle.poco.bbhelper.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

// TODO: 本当はこういう機能は、Beehiveから取得すべき
public class ResourcesCache {

    private static final String FILE_PATH_RESOURCES = "resources.json";
    
    private static ResourcesCache instance = null;

    private Map<String, Resource> cache =
            new HashMap<String, Resource>();

    // uninstanciable
    private ResourcesCache() {}

    public static ResourcesCache getInstance() {
        if (instance == null) {
            instance = new ResourcesCache();
        }
        return instance;
    }

    public Map<String, Resource> getBookableResources()
            throws JsonParseException, IOException {
        if (cache.isEmpty()) {
            loadBookableResources();
        }
        return cache;
    }

    private void loadBookableResources()
            throws JsonParseException, IOException {
        InputStream in = this.getClass().getClassLoader().
                getResourceAsStream(FILE_PATH_RESOURCES);
        List<Resource> list = new ObjectMapper().readValue(
                in, new TypeReference<List<Resource>>() {
        });
        for (Resource br : list) {
            cache.put(br.getResource_id(), br);
        }
    }

}
