package com.oracle.poco.bbhelper;

/**
 * 会議室の用途ごとの分類
 */
// TODO 会議室情報を保持するファイルのファイル名は、可変にする
// TODO 会議室の構成が変わったときに対応できる設計が必要
enum FloorCategory {
    /**
     * 社外来訪者向け会議室／セミナールーム(13,14F)
     */
    CONFERENCE_EXTERNAL("resources_conference_external.json"),
    /**
     * 社員用大型会議室(15F)
     */
    CONFERENCE_INTERNAL("resources_conference_internal.json"),
    /**
     * 執務エリア: 低階層(16-18F)
     */
    MEETING_LOWER("resources_meeting_lower.json"),
    /**
     * 執務エリア: 高階層(19-21F)
     */
    MEETING_UPPER("resources_meeting_upper.json"),
    /**
     * その他。特殊な用途のエリア(10,11,12,23,24F)
     */
    OTHER("resources_other.json"),
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
