package com.oracle.poco.bbhelper;

/**
 * 会議室の用途ごとの分類
 */
// TODO 会議室情報を保持するファイルのファイル名は、可変にする
// TODO 会議室の構成が変わったときに対応できる設計が必要
enum FloorCategory {
    /**
     * 執務エリア
     */
    WORK("resources_work.json"),
    /**
     * 低階層エリア
     */
    LOWER("resources_lower.json"),
    /**
     * その他。特殊な用途のエリア
     */
    OTHER("resources_others.json"),
    ;

    /**
     * 会議室情報を保持するファイルのファイル名
     */
    private final String jsonResource;

    /**
     * コンストラクタ
     *
     * @param jsonResource 会議室情報を保持するファイルのファイル名
     */
    FloorCategory(String jsonResource) {
        this.jsonResource = jsonResource;
    }

    /**
     * 会議室情報を保持するファイルのファイル名を返却する
     *
     * @return 会議室情報を保持するファイルのファイル名
     */
    final String getJsonResource() {
        return jsonResource;
    }

}
