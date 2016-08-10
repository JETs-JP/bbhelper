FORMAT: 1A
HOST: https://jets.jp.oracle.com/

Beehive Booking Helper (BBHelper)
=================================

本ドキュメントでは、Beehive Booking Helper(BBHelper)のサーバーサイドAPIの仕様を記述します。

BBHelperは、1)Beehive上で予約可能な会議室を探す、2)会議室を仮押さえする、という作業を、簡単に実施するためのアプリケーションです。

サーバーサイドのAPIはREST形式で提供され、フロントエンド（WebのUI）から呼び出される想定です。

セッション管理 [/session]
-------------------------

### BBHelperにログインする [GET /login]
BBHelperにログインします。<br>
Beehiveのアカウントのユーザー名／パスワードをBasic認証のヘッダーの形式で送信し、ログインに成功すると認証済みであることを表すセッションIDを返却します。<br>
以降、上記のセッションIDをヘッダーに付加してBBHelperのAPIを呼び出します。

+ Request
    + Headers

            Authorization: Basic aGlyb3NoaS5oYXlha2F3YUBvcmFjbGUuY29tOnNhbXBsZXBhc3N3b3Jk

+ Response 200 (application/json)
    + Headers

            BBH-Authorized-Session: rfsfog17jSX68TtjwXW6wIfEj0UvGkPm


会議室の操作 [/resources]
-------------------------

### 予約済みの会議を含む、会議室情報の一覧を取得する [GET /resources/invitations/list{?fromdate}{?todate}{?floor}]
会議室情報の一覧を取得します。<br>
会議室のフロアーの種類によって、取得対象を絞り込むことができます（floorパラメータの説明を参照）。<br>
各会議室の情報の情報には、クエリで指定した時間帯の会議情報も含まれます。

+ Request
    + Headers

            BBH-Authorized-Session: rfsfog17jSX68TtjwXW6wIfEj0UvGkPm

+ Parameters
    + fromdate: `2016-04-01T12:00:00.000%2b09:00` (date, required) - 開始日時<br>
                取得対象の会議情報の時間帯を指定するパラメータ。fromdateとtodateで指定された範囲に会議時間帯が被るものが対象となる<br>
                フォーマットはISO 8601拡張形式 (yyyy-MM-ddTHH:mm:ss.SSSZ)。オフセットに含まれる"+"記号は"%2b"に置き換えること（パーセントエンコーディング）<br>
    + todate:   `2016-04-01T13:00:00.000%2b09:00` (date, required) - 終了日時<br>
                取得対象の会議情報の時間帯を指定するパラメータ<br>
                フォーマットはISO 8601拡張形式 (yyyy-MM-ddTHH:mm:ss.SSSZ)。オフセットに含まれる"+"記号を"%2b"に置き換えること（パーセントエンコーディング）<br>
    + floor:    `WORK` (string, required) - フロアカテゴリー<br>
                取得対象のフロアを指定します。<br>
                - "WORK": 執務階<br>
                - "LOWER": 低層階<br>
                - "OTHER": 執務、低層以外<br>

+ Response 200 (application/json)

        {
            "fromdate": "2016-04-01T12:00:00.000+09:00"
            "todate": "2016-04-01T13:00:00.000+09:00"
            "resources": {
                [
                    {
                        "name": "JP-OAC-CONF-17006_17M1",
                        "resource_id": "334B:3BF0:bkrs:38893C00F42F38A1E0404498C8A6612B0001DDD86644",
                        "location": "OAC",
                        "capacity": 2,
                        "link": "https://layout.com/17M1",
                        "facility": {
                            "wall": "全面透明ガラス",
                            "door": "全面透明ガラス",
                            "projector": "液晶Display",
                            "phone": "IP-Phone"
                        },
                    },
                    {
                        "name": "JP-OAC-CONF-17010_17M3",
                        "resource_id": "334B:3BF0:bkrs:38893C00F42F38A1E0404498C8A6612B0001DE093ED7",
                        "location": "OAC",
                        "capacity": 12,
                        "link": "https://layout.com/17M3",
                        "facility": {
                            "wall": "上下透明ガラス、中央すりガラス、コーナー部分全面透明ガラス",
                            "door": "全面透明ガラス",
                            "projector": "プロジェクタ",
                            "phone": "IP-ConferenceStation",
                            "whiteboard": "ホワイトボード/プロジェクター投影スクリーン兼用パネル"
                        },
                        "invitaitons": {
                            [
                                {
                                    "name": "戦略会議",
                                    "invitation_id": "bbh-invt:38893C00F42F38A1E0404498C8A6612B000D34BAADAB",
                                    "resource_id": "334B:3BF0:bkrs:38893C00F42F38A1E0404498C8A6612B0001DE093ED7",
                                    "organizer": {
                                        "name": "Yosuke Arai",
                                        "address": "yosuke.arai@oracle.com",
                                        "link": "https://people.us.oracle.com/pls/oracle/f?p=8000:2:::::PERSON_ID:243488629813781"
                                    },
                                    "start": "2016-04-01T11:30:00.000+09:00",
                                    "end": "2016-04-01T12:30:00.000+09:00"
                                },
                                {
                                    "name": "定例会議",
                                    "invitation_id": "bbh-invt:38893C00F42F38A1E0404498C8A6612B000D34BAADAB",
                                    "resource_id": "334B:3BF0:bkrs:38893C00F42F38A1E0404498C8A6612B0001DDF0E78D",
                                    "organizer": {
                                        "name": "Junko Chino",
                                        "address": "juko.chino@oracle.com",
                                        "link": "https://people.us.oracle.com/pls/oracle/f?p=8000:2:::::PERSON_ID:243488629813781"
                                    },
                                    "start": "2016-04-01T12:30:00.000+09:00",
                                    "end": "2016-04-01T13:30:00.000+09:00"
                                }
                            ]
                        }
                    }
                ]
            }
        }


### 会議室情報の一覧を取得する [GET /resources/list{?floor}]
会議室情報の一覧を取得します。<br>
会議室のフロアーの種類によって、取得対象を絞り込むことができます（floorパラメータの説明を参照）。<br>
各会議室の情報の情報には、クエリで指定した時間帯の会議情報も含まれます。

+ Request
    + Headers

            BBH-Authorized-Session: rfsfog17jSX68TtjwXW6wIfEj0UvGkPm

+ Parameters
    + floor:    `WORK` (string, required) - フロアカテゴリー<br>
                取得対象のフロアを指定します。<br>
                - "WORK": 執務階<br>
                - "LOWER": 低層階<br>
                - "OTHER": 執務、低層以外<br>

+ Response 200 (application/json)

        [
            {
                "name": "JP-OAC-CONF-17006_17M1",
                "resource_id": "334B:3BF0:bkrs:38893C00F42F38A1E0404498C8A6612B0001DDD86644",
                "location": "OAC",
                "capacity": 2,
                "link": "https://layout.com/17M1",
                "facility": {
                    "wall": "全面透明ガラス",
                    "door": "全面透明ガラス",
                    "projector": "液晶Display",
                    "phone": "IP-Phone"
                }
            },
            {
                "name": "JP-OAC-CONF-17010_17M3",
                "resource_id": "334B:3BF0:bkrs:38893C00F42F38A1E0404498C8A6612B0001DE093ED7",
                "location": "OAC",
                "capacity": 12,
                "link": "https://layout.com/17M3",
                "facility": {
                    "wall": "上下透明ガラス、中央すりガラス、コーナー部分全面透明ガラス",
                    "door": "全面透明ガラス",
                    "projector": "プロジェクタ",
                    "phone": "IP-ConferenceStation",
                    "whiteboard": "ホワイトボード/プロジェクター投影スクリーン兼用パネル"
                }
            }
        ]


### 指定した会議室の情報を取得する [GET /resources/{resource_id}]
指定したIDの会議室の情報を取得します

+ Request
    + Headers

            BBH-Authorized-Session: rfsfog17jSX68TtjwXW6wIfEj0UvGkPm

+ Parameters
    + resource_id: `334B:3BF0:bkrs:38893C00F42F38A1E0404498C8A6612B0001DE093ED7` (string, required) - 情報を取得したい会議室のid<br>

+ Response 200 (application/json)

        {
            "name": "JP-OAC-CONF-17010_17M3",
            "resource_id": "334B:3BF0:bkrs:38893C00F42F38A1E0404498C8A6612B0001DE093ED7",
            "location": "OAC",
            "capacity": 12,
            "link": "https://layout.com/17M3",
            "facility": {
                "wall": "上下透明ガラス、中央すりガラス、コーナー部分全面透明ガラス",
                "door": "全面透明ガラス",
                "projector": "プロジェクタ",
                "phone": "IP-ConferenceStation",
                "whiteboard": "ホワイトボード/プロジェクター投影スクリーン兼用パネル"
            }
        }


会議の操作 [/invitations]
-------------------------

### 指定した会議の情報を取得する [GET /invitations/{invitation_id}]
登録済みの会議の情報を取得します。

+ Parameters
    + invitation_id: `bbh-invt:38893C00F42F38A1E0404498C8A6612B000D34BAADAB` (string, required) - 情報を取得したい会議のid<br>

+ Response 200 (application/json)

        {
            "name": "経営会議",
            "invitation_id": "bbh-invt:38893C00F42F38A1E0404498C8A6612B000D34BAADAB",
            "resource_id": "334B:3BF0:bkrs:38893C00F42F38A1E0404498C8A6612B0001DDF0E78D",
            "organizer": {
                "name": "Hiroshi Hayakawa",
                "address": "hiroshi.hayakawa@oracle.com",
                "link": "https://people.us.oracle.com/pls/oracle/f?p=8000:2:::::PERSON_ID:243488629813781"
            },
            "start": "2016-04-01T12:00:00.000+09:00",
            "end": "2016-04-01T13:00:00.000+09:00"
        }


### 新規会議を登録する [POST]
新たに会議を登録します。

リクエストには、会議の名前、会議室のresource_id、開始／終了時間を含むJSONオブジェクトを指定します。

+ Request (application/json)

        {
            "name": "経営会議",
            "resource_id": "334B:3BF0:bkrs:38893C00F42F38A1E0404498C8A6612B0001DDF0E78D",
            "organizer": {
                "address": "hiroshi.hayakawa@oracle.com",
            },
            "start": "2016-04-01T12:00:00.000+09:00",
            "end": "2016-04-01T13:00:00.000+09:00"
        }


+ Response 201 (application/json)
    + Headers

            Location: /invitations/bbh-invt:38893C00F42F38A1E0404498C8A6612B000D34BAADAB

    + Body

            {
                "name": "経営会議",
                "invitation_id": "bbh-invt:38893C00F42F38A1E0404498C8A6612B000D34BAADAB",
                "resource_id": "334B:3BF0:bkrs:38893C00F42F38A1E0404498C8A6612B0001DDF0E78D",
                "organizer": {
                    "name": "Hiroshi Hayakawa",
                    "address": "hiroshi.hayakawa@oracle.com",
                    "link": "https://people.us.oracle.com/pls/oracle/f?p=8000:2:::::PERSON_ID:243488629813781"
                },
                "start": "2016-04-01T12:00:00.000+09:00",
                "end": "2016-04-01T13:00:00.000+09:00"
            }


### 登録済みの会議を削除する [DELETE /invitations/{invitation_id}]
登録済みの会議を削除します。

+ Parameters
    + invitation_id: `bbh-invt:38893C00F42F38A1E0404498C8A6612B000D34BAADAB` (string, required) - 削除したい会議のid<br>

+ Response 200

