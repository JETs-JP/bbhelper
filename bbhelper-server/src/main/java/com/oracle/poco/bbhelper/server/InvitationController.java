package com.oracle.poco.bbhelper.server;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.oracle.poco.bbhelper.core.Invitation;

@RestController
@RequestMapping("/invitations")
@CrossOrigin()
public class InvitationController {

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public String createInvitation(@RequestBody Invitation invitation) {
        InvitationCache.getInstance().put(invitation);
        // 実際にはrequest_idが発行されるまで時間が必要
        // TODO: 永続化したあと、どうやって同じ会議であることを特定すれば良いのだろう
        return "request accepted.";
    }

    @RequestMapping(value = "/list",
                    method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public String createInvitations(@RequestBody List<Invitation> invitations) {
        InvitationCache.getInstance().put(invitations);
        // 実際にはrequest_idが発行されるまで時間が必要
        // TODO: 永続化したあと、どうやって同じ会議であることを特定すれば良いのだろう
        return "request accepted.";
    }

    @RequestMapping(value = "/list",
                    method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Collection<Invitation> listConflictedInvitaitons(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            ZonedDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            ZonedDateTime end) {
        return InvitationCache.getInstance().listConflictedInvitaitons(start, end);
    }

    @RequestMapping(value = "/list/unperpetuated",
                    method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Invitation> listUnperpetuatedInvitaions() {
        return InvitationCache.getInstance().listUnperpetuatedInvitaions();
    }

    @RequestMapping(value = "/list/all",
                    method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Invitation> listAllInvitaions() {
        return InvitationCache.getInstance().getAll();
    }

}
