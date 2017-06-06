package com.oracle.poco.bbhelper;

import java.time.ZonedDateTime;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oracle.poco.bbhelper.exception.BbhelperBadRequestException;
import com.oracle.poco.bbhelper.exception.BbhelperException;
import com.oracle.poco.bbhelper.exception.ErrorDescription;
import com.oracle.poco.bbhelper.model.Invitation;
import com.oracle.poco.bbhelper.model.Resource;

/**
 * 
 * Beehiveにある会議室の情報を取り扱うためのREST APIを提供するController
 * 
 * @author hiroshi.hayakawa@oracle.com
 *
 */
@RestController
@RequestMapping("/api/resources")
public class ResourceController {

    @Autowired
    private ResourceCache resourceCache;

    /**
     * BBHelperが取り扱う全ての会議室の情報を返却します。会議の情報は含まれません。
     *
     * @return BBHelperが取り扱う全ての会議室の情報
     */
    @RequestMapping(method = RequestMethod.GET)
    public Collection<Resource> listAllBookableResources() {
        return resourceCache.getCache().values();
    }

    /**
     * 指定された会議室の情報を返却します。会議の情報は含まれません。
     *
     * @param resource_id
     * @return 指定された会議室の情報
     */
    @RequestMapping(value = "/{resource_id}",
                    method = RequestMethod.GET)
    public Resource getBookableResource(
            @PathVariable("resource_id") String resource_id) {
        return resourceCache.getResource(resource_id);
    }

    /**
     * 予約された会議を含む、各会議室の情報を返却します。
     * 会議の情報は、クエリーで指定された時間帯にかぶる会議のみが含まれます。
     * 
     * @param fromdate
     * @param todate
     * @return 予約された会議を含む、各会議室の情報
     * @throws BbhelperException 
     */
    @RequestMapping(value = "/actions/fetchWithInvitations",
                    method = RequestMethod.GET)
    public ResourcesWithInvitationsInRange fetchWithInvitations(
            HttpServletRequest request,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime fromdate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime todate,
            @RequestParam(required = false) FloorCategory floor)
                    throws BbhelperException {
        if (fromdate.compareTo(todate) >= 0) {
            BbhelperException e = new BbhelperBadRequestException(
                    ErrorDescription.FROM_DATE_IS_LATER_THAN_TODATE, HttpStatus.BAD_REQUEST);
            throw e;
        }
        Session session = (Session)request.getAttribute(Constants.REQUEST_ATTR_KEY_BBH_SESSION);
        Collection<Invitation> invitations =
                session.listConflictedInvitations(fromdate, todate, floor);
        Collection<Resource> resources = resourceCache.getCache(floor).values();
        ResourcesWithInvitationsInRange retval =
                new ResourcesWithInvitationsInRange(fromdate, todate, resources);
        if (invitations != null && invitations.size() >= 0) {
            for (Invitation invitation : invitations) {
                retval.addInvitation(invitation);
            }
        }
        return retval;
    }

    /**
     * 指定した時間帯で予約可能な会議室のリストを返却します。
     *
     * @param fromdate
     * @param todate
     * @return 指定した時間帯で予約可能な会議室のリスト
     * @throws BbhelperException
     */
    @RequestMapping(value = "/actions/fetchOnlyAvailable",
            method = RequestMethod.GET)
    public ResourcesWithInvitationsInRange fetchOnlyAvailable(
            HttpServletRequest request,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime fromdate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime todate,
            @RequestParam(required = false) FloorCategory floor)
            throws BbhelperException {
        if (fromdate.compareTo(todate) >= 0) {
            BbhelperException e = new BbhelperBadRequestException(
                    ErrorDescription.FROM_DATE_IS_LATER_THAN_TODATE, HttpStatus.BAD_REQUEST);
            throw e;
        }
        Session session = (Session)request.getAttribute(Constants.REQUEST_ATTR_KEY_BBH_SESSION);
        List<Invitation> invitations =
                session.listConflictedInvitations(fromdate, todate, floor);
        Set<String> bookedResourceIds = new HashSet<>(invitations.size());
        invitations.forEach(i -> bookedResourceIds.add(i.getResource_id()));
        Collection<Resource> resources = resourceCache.getCache(floor).values();
        Set<Resource> availableResources = new HashSet<>(resources.size());
        for (Resource resource : resources) {
            if (!bookedResourceIds.contains(resource.getResourceId())) {
                availableResources.add(resource);
            }
        }
        return new ResourcesWithInvitationsInRange(fromdate, todate, availableResources);
    }

}
