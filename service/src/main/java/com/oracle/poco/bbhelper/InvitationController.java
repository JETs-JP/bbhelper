package com.oracle.poco.bbhelper;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.oracle.poco.bbhelper.exception.BbhelperException;
import com.oracle.poco.bbhelper.model.Invitation;

@RestController
@RequestMapping("/invitations")
public class InvitationController {

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public String createInvitation(@RequestBody Invitation invitation) {
        InvitationCache.getInstance().put(invitation);
        // TODO Implement
        return "request accepted.";
    }

    @RequestMapping(value = "/list",
                    method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public String createInvitations(@RequestBody List<Invitation> invitations) {
        InvitationCache.getInstance().put(invitations);
        // TODO Implement
        return "request accepted.";
    }

    @RequestMapping(value = "/list",
                    method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Collection<Invitation> listConflictedInvitaitons(
            @RequestHeader(SessionPool.HEADER_KEY_BBH_AUTHORIZED_SESSION)
            String session_id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            ZonedDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            ZonedDateTime end) throws BbhelperException {
        TimeoutManagedContext context =
                SessionPool.getInstance().get(session_id);
        return InvitationUtils.listConflictedInvitaitons(start, end, context);
    }

}
