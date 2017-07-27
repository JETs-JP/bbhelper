package com.oracle.poco.bbhelper;

public class InvitationControllerItConstants {

    static final String REQUEST_CREATE_INVITATION =
            "{" +
            "    \"name\": \"テスト会議\"," +
            "    \"resource_id\": \"334B:3BF0:bkrs:38893C00F42F38A1E0404498C8A6612B0000BFAF005A\"," +
            "    \"start\": \"2017-07-19T22:00:00.000+09:00\"," +
            "    \"end\": \"2017-07-19T23:00:00.000+09:00\"" +
            "}";

    static final String REQUEST_CREATE_INVITATION_WITHOUT_NAME =
            "{" +
            "    \"resource_id\": \"334B:3BF0:bkrs:38893C00F42F38A1E0404498C8A6612B0000BFAF005A\"," +
            "    \"start\": \"2017-07-19T22:00:00.000+09:00\"," +
            "    \"end\": \"2017-07-19T23:00:00.000+09:00\"" +
            "}";

    static final String RESPONSE_CREATE_INVITATION_WITHOUT_NAME =
            "{" +
            "    \"status\": 400," +
            "    \"error\": \"Bad Request\"," +
            "    \"code\": \"com.oracle.poco.bbhelper.exception.BbhelperValidationFailureException\"," +
            "    \"message\": \"Invalid request body or parameter(s).\"," +
            "    \"details\": [" +
            "        \"may not be null\"" +
            "    ]" +
            "}";

    static final String REQUEST_CREATE_INVITATION_WITHOUT_RESOURCE_ID =
            "{" +
            "    \"name\": \"テスト会議\"," +
            "    \"start\": \"2017-07-19T22:00:00.000+09:00\"," +
            "    \"end\": \"2017-07-19T23:00:00.000+09:00\"" +
            "}";

    static final String RESPONSE_CREATE_INVITATION_WITHOUT_RESOURCE_ID =
            "{" +
            "    \"status\": 400," +
            "    \"error\": \"Bad Request\"," +
            "    \"code\": \"com.oracle.poco.bbhelper.exception.BbhelperValidationFailureException\"," +
            "    \"message\": \"Invalid request body or parameter(s).\"," +
            "    \"details\": [" +
            "        \"Resource doesn't exist.\"," +
            "        \"may not be null\"" +
            "    ]" +
            "}";

    static final String REQUEST_CREATE_INVITATION_WITH_INVALID_RESOURCE_ID =
            "{" +
            "    \"name\": \"テスト会議\"," +
            "    \"resource_id\": \"334B:3BF0:bkrs:38893C00F42F38A1E0404498C8A6612B0000BFAF005A_\"," +
            "    \"start\": \"2017-07-19T22:00:00.000+09:00\"," +
            "    \"end\": \"2017-07-19T23:00:00.000+09:00\"" +
            "}";

    static final String RESPONSE_CREATE_INVITATION_WITH_INVALID_RESOURCE_ID =
            "{" +
            "    \"status\": 400," +
            "    \"error\": \"Bad Request\"," +
            "    \"code\": \"com.oracle.poco.bbhelper.exception.BbhelperValidationFailureException\"," +
            "    \"message\": \"Invalid request body or parameter(s).\"," +
            "    \"details\": [" +
            "        \"Resource doesn't exist.\"" +
            "    ]" +
            "}";

    static final String RESPONSE_NO_SESSION_ID =
            "{" +
            "    \"status\": 401," +
            "    \"error\": \"Unauthorized\"," +
            "    \"code\": \"com.oracle.poco.bbhelper.exception.BbhelperNoSessionIdException\"," +
            "    \"message\": \"No session id.\"" +
            "}";

    static final String RESPONSE_INVALID_SESSION_ID =
            "{" +
            "    \"status\": 401," +
            "    \"error\": \"Unauthorized\"," +
            "    \"code\": \"com.oracle.poco.bbhelper.exception.BbhelperInvalidSessionIdException\"," +
            "    \"message\": \"The Session has been expired or didn't exist.\"" +
            "}";

}
