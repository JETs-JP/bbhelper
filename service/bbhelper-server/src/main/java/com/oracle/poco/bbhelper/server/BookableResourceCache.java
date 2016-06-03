package com.oracle.poco.bbhelper.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.oracle.poco.bbhelper.core.BookableResource;
import com.oracle.poco.bbhelper.core.BookableResourceLoader;

class BookableResourceCache {

    private Map<String, BookableResource> cache =
            new HashMap<String, BookableResource>();

    private static BookableResourceCache instance = null;

    static BookableResourceCache getInstance() {
        initialize();
        return instance;
    }

    static void initialize() {
        if (instance == null) {
            instance = new BookableResourceCache();
            instance.loadBookableResources();
        }
    }

    private void loadBookableResources() {
        try {
            cache = BookableResourceLoader.getInstance().getBookableResources();
        } catch (IOException e) {
            LoggerManager.getLogger().severe(
                    "failed to load bookable resources data.");;
            e.printStackTrace();
            System.exit(1);
        }
    }

    Collection<BookableResource> getAllResources() {
        List<BookableResource> retval = new ArrayList<BookableResource>();
        for (BookableResource resource : cache.values()) {
            retval.add(BookableResource.deepClone(resource));
        }
        return retval;
    }

    BookableResource get(String resource_id) {
        // null check不要。reource_idがnullならnullを返す
        BookableResource origin = cache.get(resource_id);
        if (origin == null) {
            return null;
        }
        return BookableResource.deepClone(origin);
    }

}
