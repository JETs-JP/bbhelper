package com.oracle.poco.bbhelper;

enum FloorCategory {
    WORK("resources_work.json"),
    LOWER("resources_lower.json"),
    OTHER("resources_others.json"),
    ;

    private final String jsonResource;

    private FloorCategory(String jsonResource) {
        this.jsonResource = jsonResource;
    }

    final String getJsonResource() {
        return jsonResource;
    }

    static final FloorCategory getDafault() {
        return FloorCategory.WORK;
    }

}
