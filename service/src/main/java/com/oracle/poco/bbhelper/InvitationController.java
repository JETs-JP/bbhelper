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

import com.oracle.poco.bbhelper.exception.BbhelperBadRequestException;
import com.oracle.poco.bbhelper.exception.BbhelperException;
import com.oracle.poco.bbhelper.exception.ErrorDescription;
import com.oracle.poco.bbhelper.log.BbhelperLogger;
import com.oracle.poco.bbhelper.model.Invitation;

@RestController
@RequestMapping("/invitations")
public class InvitationController {

    @Autowired
    BbhelperLogger logger;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public String createInvitation(HttpServletRequest request,
            @RequestBody Invitation invitation)
                    throws BbhelperException {
        TimeoutManagedContext context = null;
        String calendar_id = null;
        String result = null;
        try {
            context = (TimeoutManagedContext) request.getAttribute(
                    Constants.REQUEST_ATTR_KEY_BEEHIVE_CONTEXT);
            calendar_id = InvitationUtils.getDefaultCalendar(context);
            result = InvitationUtils.createInvitaion(invitation, calendar_id, context);
        } catch (BbhelperException e) {
            logger.logBbhelperException(request, e);
            throw e;
        }
        // TODO レスポンスとしてInvitationsを返す
        return result;
    }

    @RequestMapping(value = "/list",
                    method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public String createInvitations(@RequestBody List<Invitation> invitations) {
        // TODO Implement
        return "request accepted.";
    }

    @RequestMapping(value = "/list",
                    method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Collection<Invitation> listConflictedInvitaitons(
            HttpServletRequest request,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            ZonedDateTime fromdate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            ZonedDateTime todate,
            @RequestParam(required = false) FloorCategory floor)
                    throws BbhelperException {
        try {
            if (fromdate.compareTo(todate) >= 0) {
                BbhelperException e = new BbhelperBadRequestException(
                        ErrorDescription.FROM_DATE_IS_LATER_THAN_TODATE);
                throw e;
            }

            TimeoutManagedContext context = (TimeoutManagedContext) request.
                    getAttribute(Constants.REQUEST_ATTR_KEY_BEEHIVE_CONTEXT);
            Collection<Invitation> retval = null;
            retval = InvitationUtils.listConflictedInvitaitons(
                    fromdate, todate, floor, context);
            return retval;
        } catch (BbhelperException e) {
            logger.logBbhelperException(request, e);
            throw e;
        }
    }

}
