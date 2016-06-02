FORMAT: 1A
HOST: https://example/

Beehive Booking Helper (BBHelper)
=================================

Beehive Booking Helper(BBHelper)のサーバーサイドのAPIドキュメントです。

BBHelperは、Beehiveで予約可能な会議室を探して仮押さえする作業を、簡単に実施するためのアプリケーションです。

## 会議室の操作 [/resources]
<!-- リンクを追加 -->

### 各会議室の指定した時間帯の会議を取得する [GET /resources/invitations/list{?fromdate}{?todate}]
各会議室の、クエリで指定した時間帯の会議を取得します。併せて各会議室の設備の情報も含めて返します。

+ Parameters
    + fromdate: `2016-04-01T12:00:00.000%2b09:00` (date, required) - 開始日時<br>
                フォーマットはISO 8601拡張形式 (yyyy-MM-ddTHH:mm:ss.SSSZ)で、オフセットの"+"記号を"%2b"に置き換えた文字列（パーセントエンコーディング）<br>
    + todate:   `2016-04-01T13:00:00.000%2b09:00` (date, required) - 終了日時<br>
                フォーマットはISO 8601拡張形式 (yyyy-MM-ddTHH:mm:ss.SSSZ)で、オフセットの"+"記号を"%2b"に置き換えた文字列（パーセントエンコーディング）<br>

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
                        "name": "JP-OAC-CONF-17008_17M2",
                        "resource_id": "334B:3BF0:bkrs:38893C00F42F38A1E0404498C8A6612B0001DDF0E78D",
                        "location": "OAC",
                        "capacity": 8,
                        "link": "https://layout.com/17M2",
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
                            ]
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


### 全ての会議室の情報を取得する [GET /resources/list]
予約可能な全ての会議室の情報を取得します。会議の情報は含まれません。

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
            }, {
                "name": "JP-OAC-CONF-17008_17M2",
                "resource_id": "334B:3BF0:bkrs:38893C00F42F38A1E0404498C8A6612B0001DDF0E78D",
                "location": "OAC",
                "capacity": 8,
                "link": "https://layout.com/17M2",
                "facility": {
                    "wall": "上下透明ガラス、中央すりガラス、コーナー部分全面透明ガラス",
                    "door": "全面透明ガラス",
                    "projector": "プロジェクタ",
                    "phone": "IP-ConferenceStation",
                    "whiteboard": "ホワイトボード/プロジェクター投影スクリーン兼用パネル"
                }
            }, {
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


### 指定した会議室の情報を取得する [GET /resources/bookable/{resource_id}]
指定したIDの会議室の情報を取得します

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


## 登録した会議の操作 [/invitations]

### 登録済みの会議の情報を取得する [GET /invitations/{invitation_id}]
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


### 会議を登録する [POST]
会議を登録します。

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

<!--
以下メモ


Resource Viewerの呼出しシーケンス

1. 初期画面（会議室のカレンダーの一覧）表示
    - 会議室の一覧を、beehive本体とは別のアプリケーションに問い合わせて取得している
    - 多分、会議室の一覧をbeehiveのAPIから直接取ることはできない
    - って言うかその別のアプリケーション、認証無しで呼べたわ

2-1. 指定された日付の予約一覧を取得
    - invt/list/byRange

2-2. 各予約の詳細情報を取得
    - invt/read

bkrs/{id} で、リソースの情報を取得

条件に合致する会議室のカレンダーを取ってくる
指定された日時範囲でカレンダーの予約を取得する(invt/list/byRange)
    繰り返す
予約がなかったカレンダーのリソースidを取得する
    →手作業でリストを作る
    →JSONデータとしてファイルに書き出しておく
        カレンダーとの対応
        リソースの情報
リソースの情報を取得する
クライアントにリソース一覧を返す




指定されたリソースの詳細情報を返す


FUKUOKA
NAGOYA
OAC
OSAKA
SAPPORO
TOHOKU
TOKYO10
TOYOTA

CONF
DEMO
VIDEO
PROJ
TEL
VEHICLE


-->
