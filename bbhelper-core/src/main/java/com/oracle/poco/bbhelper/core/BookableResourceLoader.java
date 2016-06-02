package com.oracle.poco.bbhelper.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

// TODO: 本当はこういう機能は、Beehiveから取得すべき
public class BookableResourceLoader {

    private static final String FILE_PATH_RESOURCES = "resources.json";
    
    private static BookableResourceLoader instance = null;

    private Map<String, BookableResource> cache =
            new HashMap<String, BookableResource>();

    // uninstanciable
    private BookableResourceLoader() {}

    public static BookableResourceLoader getInstance() {
        if (instance == null) {
            instance = new BookableResourceLoader();
        }
        return instance;
    }

    public Map<String, BookableResource> getBookableResources()
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
        List<BookableResource> list = new ObjectMapper().readValue(
                in, new TypeReference<List<BookableResource>>() {
        });
        for (BookableResource br : list) {
            cache.put(br.getResource_id(), br);
        }
    }

}
