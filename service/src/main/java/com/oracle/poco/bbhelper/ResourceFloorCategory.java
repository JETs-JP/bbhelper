package com.oracle.poco.bbhelper;

import com.fasterxml.jackson.annotation.JsonValue;

enum ResourceFloorCategory {
    WORK_FLOOR("work", "resources_work.json"),
    LOWER_FLOOR("lower", "resources_lower.json"),
    OTHER_FLOOR("other", "resources_other.json"),
    ;

    private final String label;

    private final String jsonResource;

    private ResourceFloorCategory(String label, String jsonResource) {
        this.label = label;
        this.jsonResource = jsonResource;
    }

    static final ResourceFloorCategory fromLabel(String label) {
        for (ResourceFloorCategory category : ResourceFloorCategory.values()) {
            if (category.label == label) {
                return category;
            }
        }
        throw new IllegalArgumentException();
    }

    @JsonValue
    String getLabel() {
        return label;
    }

    String getJsonResource() {
        return jsonResource;
    }

}
