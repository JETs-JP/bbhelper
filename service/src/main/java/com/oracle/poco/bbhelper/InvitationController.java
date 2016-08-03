package com.oracle.poco.bbhelper;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.oracle.poco.bbhelper.exception.BbhelperException;
import com.oracle.poco.bbhelper.log.BbhelperLogger;
import com.oracle.poco.bbhelper.model.Invitation;

@RestController
@RequestMapping("/invitations")
public class InvitationController {

    @Autowired
    BbhelperLogger logger;

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
            HttpServletRequest request,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            ZonedDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            ZonedDateTime end,
            @RequestParam String floorCategory) throws BbhelperException {
        TimeoutManagedContext context = (TimeoutManagedContext) request.
                getAttribute(Constants.REQUEST_ATTR_KEY_BEEHIVE_CONTEXT);
        Collection<Invitation> retval = null;
        try {
            // TODO floorCategoryをパラメータから直接取りたい
            retval = InvitationUtils.listConflictedInvitaitons(start, end,
                    FloorCategory.fromLabel(floorCategory), context);
        } catch (BbhelperException e) {
            logger.logBbhelperException(request, e);
            throw e;
        }
        return retval;
    }

}
