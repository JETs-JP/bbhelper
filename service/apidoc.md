FORMAT: 1A
HOST: https://example/

Beehive Booking Helper (BBHelper)
=================================

本ドキュメントでは、Beehive Booking Helper(BBHelper)のサーバーサイドAPIの仕様を記述します。

BBHelperは、1)Beehive上で予約可能な会議室を探す、2)会議室を仮押さえする、という作業を、簡単に実施するためのアプリケーションです。

サーバーサイドのAPIはREST形式で提供され、フロントエンド（WebのUI）から呼び出される想定です。

## 認可 [/authz]

### 認可トークンを取得する [GET]
各会議室の、クエリで指定した時間帯の会議を取得します。併せて各会議室の設備の情報も含めて返します。

<!--
+ Parameters
    + fromdate: `2016-04-01T12:00:00.000%2b09:00` (date, required) - 開始日時<br>
                フォーマットはISO 8601拡張形式 (yyyy-MM-ddTHH:mm:ss.SSSZ)。オフセットに含まれる"+"記号は"%2b"に置き換えること（パーセントエンコーディング）<br>
    + todate:   `2016-04-01T13:00:00.000%2b09:00` (date, required) - 終了日時<br>
                フォーマットはISO 8601拡張形式 (yyyy-MM-ddTHH:mm:ss.SSSZ)。オフセットに含まれる"+"記号を"%2b"に置き換えること（パーセントエンコーディング）<br>
-->

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


## 会議室の操作 [/resources]

### 各会議室の指定した時間帯の会議を取得する [GET /resources/invitations/list{?fromdate}{?todate}]
各会議室の、クエリで指定した時間帯の会議を取得します。併せて各会議室の設備の情報も含めて返します。

+ Parameters
    + fromdate: `2016-04-01T12:00:00.000%2b09:00` (date, required) - 開始日時<br>
                フォーマットはISO 8601拡張形式 (yyyy-MM-ddTHH:mm:ss.SSSZ)。オフセットに含まれる"+"記号は"%2b"に置き換えること（パーセントエンコーディング）<br>
    + todate:   `2016-04-01T13:00:00.000%2b09:00` (date, required) - 終了日時<br>
                フォーマットはISO 8601拡張形式 (yyyy-MM-ddTHH:mm:ss.SSSZ)。オフセットに含まれる"+"記号を"%2b"に置き換えること（パーセントエンコーディング）<br>

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


### 全ての会議室の設備情報を取得する [GET /resources/list]
予約可能な全ての会議室の設備情報を取得します。このAPIの応答には、会議の情報は含まれません。

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


### 指定した会議室の設備情報を取得する [GET /resources/{resource_id}]
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


## 会議の操作 [/invitations]

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

