package com.oracle.poco.bbhelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.oracle.poco.bbhelper.Constants.HEADER_KEY_BBH_AUTHORIZED_SESSION;
import static com.oracle.poco.bbhelper.ItConstants.PROPERTY_KEY_TEST_BEEHIVE_PASSWORD;
import static com.oracle.poco.bbhelper.ItConstants.PROPERTY_KEY_TEST_BEEHIVE_USERNAME;
import static com.oracle.poco.bbhelper.ItUtils.makeBasicAuthString;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = {Application.class},
        initializers = {ConfigFileApplicationContextInitializer.class})
@WebAppConfiguration
public class SessionControllerIT {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private String username, password;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        this.username = System.getProperty(PROPERTY_KEY_TEST_BEEHIVE_USERNAME);
        this.password = System.getProperty(PROPERTY_KEY_TEST_BEEHIVE_PASSWORD);
    }

    private static final String RESPONSE_NO_CREDENTIALS =
            "{" +
            "   \"status\": 401," +
            "   \"error\": \"Unauthorized\"," +
            "   \"code\": \"com.oracle.poco.bbhelper.exception.BbhelperNoCredentialsException\"," +
            "   \"message\": \"No credentials.\"" +
            "}";

    private static final String RESPONSE_INVALID_CREDENTIALS =
            "{" +
            "   \"status\": 401," +
            "   \"error\": \"Unauthorized\"," +
            "   \"code\": \"com.oracle.poco.bbhelper.exception.BbhelperInvalidCredentialsException\"," +
            "   \"message\": \"Incorrect username or password.\"" +
            "}";

    @Test
    public void NoCredentials() throws Exception {
        mockMvc.perform(get("/api/session/actions/login"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json(RESPONSE_NO_CREDENTIALS, true));
    }

    @Test
    public void InvalidUsername() throws Exception {
        mockMvc.perform(get("/api/session/actions/login")
                .header(HttpHeaders.AUTHORIZATION,
                        makeBasicAuthString(username + "_", password)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json(RESPONSE_INVALID_CREDENTIALS, true));
    }

    @Test
    public void InvalidPassword() throws Exception {
        mockMvc.perform(get("/api/session/actions/login")
                .header(HttpHeaders.AUTHORIZATION,
                        makeBasicAuthString(username,password + "_")))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json(RESPONSE_INVALID_CREDENTIALS, true));
    }

    @Test
    public void Success() throws Exception {
        mockMvc.perform(get("/api/session/actions/login")
                .header(HttpHeaders.AUTHORIZATION, makeBasicAuthString(username, password)))
                .andExpect(status().isOk())
                .andExpect(header().string(
                        HEADER_KEY_BBH_AUTHORIZED_SESSION, not(isEmptyOrNullString())));
    }

}
