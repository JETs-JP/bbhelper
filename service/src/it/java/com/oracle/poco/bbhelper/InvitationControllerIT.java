package com.oracle.poco.bbhelper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.oracle.poco.bbhelper.model.Invitation;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.ZonedDateTime;

import static com.oracle.poco.bbhelper.Constants.HEADER_KEY_BBH_AUTHORIZED_SESSION;
import static com.oracle.poco.bbhelper.ItConstants.PROPERTY_KEY_TEST_BEEHIVE_PASSWORD;
import static com.oracle.poco.bbhelper.ItConstants.PROPERTY_KEY_TEST_BEEHIVE_USERNAME;
import static com.oracle.poco.bbhelper.ItUtils.makeBasicAuthString;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = {Application.class},
        initializers = {ConfigFileApplicationContextInitializer.class})
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InvitationControllerIT {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private String username, password;

    private String sessionId;

    private ObjectMapper objectMapper;

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        this.username = System.getProperty(PROPERTY_KEY_TEST_BEEHIVE_USERNAME);
        this.password = System.getProperty(PROPERTY_KEY_TEST_BEEHIVE_PASSWORD);
        this.sessionId = mockMvc.perform(get("/api/session/actions/login")
                .header(HttpHeaders.AUTHORIZATION, makeBasicAuthString(username, password)))
                .andExpect(status().isOk())
                .andExpect(header().string(
                        HEADER_KEY_BBH_AUTHORIZED_SESSION, not(isEmptyOrNullString())))
                .andReturn().getResponse().getHeader(HEADER_KEY_BBH_AUTHORIZED_SESSION);
        this.objectMapper = (new ObjectMapper())
                .registerModule((new SimpleModule()).addDeserializer(
                        ZonedDateTime.class, new ZonedDateTimeDeserializer()));
    }

    private static final String REQUEST_BODY_CREATE_INVITATION =
            "{" +
            "    \"name\": \"テスト会議\"," +
            "    \"resource_id\": \"334B:3BF0:bkrs:38893C00F42F38A1E0404498C8A6612B0000BFAF005A\"," +
            "    \"start\": \"2017-07-19T22:00:00.000+09:00\"," +
            "    \"end\": \"2017-07-19T23:00:00.000+09:00\"" +
            "}";

    @Test
    public void Success() throws Exception {
        Invitation created = createInvitation(this.sessionId, REQUEST_BODY_CREATE_INVITATION);
        getInvitation(this.sessionId, created);
        deleteInvitation(this.sessionId, created);
    }

    private Invitation createInvitation(String sessionId, String requestBody) throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/api/invitations")
                .header(HEADER_KEY_BBH_AUTHORIZED_SESSION, sessionId)
                .header(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn();
        Invitation actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), Invitation.class);
        InvitationCommitter expected =
                objectMapper.readValue(requestBody, InvitationCommitter.class);
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getResourceId(), actual.getResourceId());
        assertEquals(expected.getStart(), actual.getStart());
        assertEquals(expected.getEnd(), actual.getEnd());
        return actual;
    }

    private void getInvitation(String sessionId, Invitation expected) throws Exception {
        MvcResult mvcResult =
                mockMvc.perform(get("/api/invitations/" + expected.getInvitationId())
                .header(HEADER_KEY_BBH_AUTHORIZED_SESSION, sessionId))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn();
        Invitation actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), Invitation.class);
        assertEquals(expected, actual);
    }

    private void deleteInvitation(String sessionId, Invitation target) throws Exception {
        mockMvc.perform(delete("/api/invitations/" + target.getInvitationId())
                .header(HEADER_KEY_BBH_AUTHORIZED_SESSION, sessionId))
                .andExpect(status().isNoContent())
                .andReturn();
        mockMvc.perform(get("/api/invitations/" + target.getInvitationId())
                .header(HEADER_KEY_BBH_AUTHORIZED_SESSION, sessionId))
                .andExpect(status().isNotFound());
    }

}
