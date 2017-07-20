package com.oracle.poco.bbhelper;

import com.oracle.poco.bbhelper.exception.BbhelperException;
import com.oracle.poco.bbhelper.exception.BbhelperResourceNotFoundException;
import com.oracle.poco.bbhelper.log.BbhelperLogger;
import com.oracle.poco.bbhelper.log.ErrorMessage;
import com.oracle.poco.bbhelper.log.Operation;
import com.oracle.poco.bbhelper.model.Invitation;
import com.oracle.poco.bbhelper.model.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    BbhelperLogger logger = BbhelperLogger.getLogger(ResourceController.class);

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
            @PathVariable("resource_id") String resource_id) throws BbhelperException {
        Resource resource = resourceCache.getResource(resource_id);
        if (resource == null) {
            BbhelperException e = new BbhelperResourceNotFoundException();
            logger.info(new ErrorMessage(Operation.PULL_RESOURCES_FROM_CACHE, e));
            throw e;
        }
        return resource;
    }

    /**
     * 予約された会議を含む、各会議室の情報を返却します。
     * 会議の情報は、クエリーで指定された時間帯にかぶる会議のみが含まれます。
     * 
     * @param duration
     * @return 予約された会議を含む、各会議室の情報
     * @throws BbhelperException 
     */
    @RequestMapping(value = "/actions/fetchWithInvitations",
                    method = RequestMethod.GET)
    public ResourcesWithInvitationsInRange fetchWithInvitations(
            HttpServletRequest request,
            @ModelAttribute @Validated Duration duration,
            @RequestParam(required = false) FloorCategory floor)
                    throws BbhelperException {
        Session session = (Session)request.getAttribute(Constants.REQUEST_ATTR_KEY_BBH_SESSION);
        Collection<Invitation> invitations = session.listConflictedInvitations(
                duration.getFromdate(), duration.getTodate(), floor);
        Collection<Resource> resources = resourceCache.getCache(floor).values();
        ResourcesWithInvitationsInRange retval = new ResourcesWithInvitationsInRange(
                duration.getFromdate(), duration.getTodate(), resources);
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
     * @param duration
     * @return 指定した時間帯で予約可能な会議室のリスト
     * @throws BbhelperException
     */
    @RequestMapping(value = "/actions/fetchOnlyAvailable",
            method = RequestMethod.GET)
    public ResourcesWithInvitationsInRange fetchOnlyAvailable(
            HttpServletRequest request,
            @ModelAttribute @Validated Duration duration,
            @RequestParam(required = false) FloorCategory floor)
            throws BbhelperException {
        Session session = (Session)request.getAttribute(Constants.REQUEST_ATTR_KEY_BBH_SESSION);
        List<Invitation> invitations = session.listConflictedInvitations(
                duration.getFromdate(), duration.getTodate(), floor);
        Set<String> bookedResourceIds = new HashSet<>(invitations.size());
        invitations.forEach(i -> bookedResourceIds.add(i.getResourceId()));
        Collection<Resource> resources = resourceCache.getCache(floor).values();
        Set<Resource> availableResources = new HashSet<>(resources.size());
        for (Resource resource : resources) {
            if (!bookedResourceIds.contains(resource.getResourceId())) {
                availableResources.add(resource);
            }
        }
        return new ResourcesWithInvitationsInRange(
                duration.getFromdate(), duration.getTodate(), availableResources);
    }

}
