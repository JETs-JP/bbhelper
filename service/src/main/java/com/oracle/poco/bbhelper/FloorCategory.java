package com.oracle.poco.bbhelper;

import com.fasterxml.jackson.annotation.JsonValue;

enum FloorCategory {
    WORK_FLOOR("work", "resources_work.json"),
    LOWER_FLOOR("lower", "resources_lower.json"),
    OTHER_FLOOR("other", "resources_others.json"),
    ;

    private final String label;

    private final String jsonResource;

    private FloorCategory(String label, String jsonResource) {
        this.label = label;
        this.jsonResource = jsonResource;
    }

    static final FloorCategory fromLabel(String label) {
        for (FloorCategory category : FloorCategory.values()) {
            if (category.label.equals(label)) {
                return category;
            }
        }
        throw new IllegalArgumentException();
    }

    @JsonValue
    final String getLabel() {
        return label;
    }

    final String getJsonResource() {
        return jsonResource;
    }

    static final FloorCategory getDafault() {
        return FloorCategory.WORK_FLOOR;
    }

}
