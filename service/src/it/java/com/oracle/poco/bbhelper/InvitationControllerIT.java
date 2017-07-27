package com.oracle.poco.bbhelper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.oracle.poco.bbhelper.model.Invitation;
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
public class InvitationControllerIT {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private String username, password;

    private String sessionId;

    private ObjectMapper objectMapper;

    private String INVITATION_ID_DUMMY =
            "334B:3BF0:invt:A8E58561C9AD4E0F9818AEE271C7F530000000000096";
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

    @Test
    public void Success() throws Exception {
        Invitation created = createInvitation(this.sessionId, InvitationControllerItConstants.REQUEST_CREATE_INVITATION);
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
                mockMvc.perform(get("/api/invitations/{invitation_id}", expected.getInvitationId())
                .header(HEADER_KEY_BBH_AUTHORIZED_SESSION, sessionId))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn();
        Invitation actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), Invitation.class);
        assertEquals(expected, actual);
    }

    private void deleteInvitation(String sessionId, Invitation target) throws Exception {
        mockMvc.perform(delete("/api/invitations/{invitation_id}", target.getInvitationId())
                .header(HEADER_KEY_BBH_AUTHORIZED_SESSION, sessionId))
                .andExpect(status().isNoContent())
                .andReturn();
        mockMvc.perform(get("/api/invitations/" + target.getInvitationId())
                .header(HEADER_KEY_BBH_AUTHORIZED_SESSION, sessionId))
                .andExpect(status().isNotFound());
    }

    /*
     * Bad Request
     */

    @Test
    public void createInvitationWithRequestWithoutName() throws Exception {
        mockMvc.perform(post("/api/invitations")
                .header(HEADER_KEY_BBH_AUTHORIZED_SESSION, this.sessionId)
                .header(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .content(InvitationControllerItConstants.REQUEST_CREATE_INVITATION_WITHOUT_NAME))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json(InvitationControllerItConstants.RESPONSE_CREATE_INVITATION_WITHOUT_NAME, true));
    }

    @Test
    public void createInvitationWithRequestWithoutResourceId() throws Exception {
        mockMvc.perform(post("/api/invitations")
                .header(HEADER_KEY_BBH_AUTHORIZED_SESSION, this.sessionId)
                .header(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .content(InvitationControllerItConstants.REQUEST_CREATE_INVITATION_WITHOUT_RESOURCE_ID))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json(InvitationControllerItConstants.RESPONSE_CREATE_INVITATION_WITHOUT_RESOURCE_ID, true));
    }

    @Test
    public void createInvitationWithRequestWithInvalidResourceId() throws Exception {
        mockMvc.perform(post("/api/invitations")
                .header(HEADER_KEY_BBH_AUTHORIZED_SESSION, this.sessionId)
                .header(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .content(InvitationControllerItConstants.REQUEST_CREATE_INVITATION_WITH_INVALID_RESOURCE_ID))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json(InvitationControllerItConstants.RESPONSE_CREATE_INVITATION_WITH_INVALID_RESOURCE_ID, true));
    }

    /*
     * Unauthorized
     */

    @Test
    public void createInvitationWithNoSessionId() throws Exception {
        mockMvc.perform(post("/api/invitations")
                .header(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .content(InvitationControllerItConstants.REQUEST_CREATE_INVITATION))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json(InvitationControllerItConstants.RESPONSE_NO_SESSION_ID, true));
    }

    @Test
    public void getInvitationWithNoSessionId() throws Exception {
        mockMvc.perform(delete("/api/invitations/{invitation_id}", INVITATION_ID_DUMMY))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json(InvitationControllerItConstants.RESPONSE_NO_SESSION_ID, true));
    }

    @Test
    public void deleteInvitationWithNoSessionId() throws Exception {
        mockMvc.perform(delete("/api/invitations/{invitation_id}", INVITATION_ID_DUMMY))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json(InvitationControllerItConstants.RESPONSE_NO_SESSION_ID, true));
    }

    @Test
    public void createInvitationWithInvalidSessionId() throws Exception {
        mockMvc.perform(post("/api/invitations")
                .header(HEADER_KEY_BBH_AUTHORIZED_SESSION, this.sessionId + "_")
                .header(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .content(InvitationControllerItConstants.REQUEST_CREATE_INVITATION))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json(InvitationControllerItConstants.RESPONSE_INVALID_SESSION_ID, true));
    }

    @Test
    public void getInvitationWithInvalidSessionId() throws Exception {
        mockMvc.perform(get("/api/invitations/{invitation_id}", INVITATION_ID_DUMMY)
                .header(HEADER_KEY_BBH_AUTHORIZED_SESSION, this.sessionId + "_"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json(InvitationControllerItConstants.RESPONSE_INVALID_SESSION_ID, true));
    }

    @Test
    public void deleteInvitationWithInvalidSessionId() throws Exception {
        mockMvc.perform(delete("/api/invitations/{invitation_id}", INVITATION_ID_DUMMY)
                .header(HEADER_KEY_BBH_AUTHORIZED_SESSION, this.sessionId + "_"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json(InvitationControllerItConstants.RESPONSE_INVALID_SESSION_ID, true));
    }

}
