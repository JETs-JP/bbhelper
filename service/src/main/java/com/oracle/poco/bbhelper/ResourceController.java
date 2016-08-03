package com.oracle.poco.bbhelper;

import java.time.ZonedDateTime;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oracle.poco.bbhelper.exception.BbhelperException;
import com.oracle.poco.bbhelper.log.BbhelperLogger;
import com.oracle.poco.bbhelper.model.Invitation;
import com.oracle.poco.bbhelper.model.ResourceWithInvitationsInRange;
import com.oracle.poco.bbhelper.model.ResourcesWithInvitationsInRange;

/**
 * 
 * 会議室の情報を取り扱うためのREST APIを提供するController
 * 
 * @author hiroshi.hayakawa@oracle.com
 *
 */
@RestController
@RequestMapping("/resources")
public class ResourceController {

    @Autowired
    private BbhelperLogger logger;

    /**
     * 予約された会議を含む、各会議室の情報を返却します。
     * 会議の情報は、クエリーで指定された時間帯にかぶる会議のみが含まれます。
     * 
     * @param fromdate
     * @param todate
     * @return 予約された会議を含む、各会議室の情報
     * @throws BbhelperException 
     */
    @RequestMapping(value = "/invitations/list",
                    method = RequestMethod.GET)
    public ResourcesWithInvitationsInRange listInvitations(
            HttpServletRequest request,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            ZonedDateTime fromdate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            ZonedDateTime todate,
            @RequestParam String floorCategory) throws BbhelperException {
        TimeoutManagedContext context = (TimeoutManagedContext) request.
                getAttribute(Constants.REQUEST_ATTR_KEY_BEEHIVE_CONTEXT);
        final Collection<Invitation> invitations;
        try {
            // TODO floorCategoryをパラメータから直接取りたい
            invitations = InvitationUtils.listConflictedInvitaitons(
                    fromdate, todate, 
                    FloorCategory.fromLabel(floorCategory), context);
        } catch (BbhelperException e) {
            logger.logBbhelperException(request, e);
            throw e;
        }

        Collection<ResourceWithInvitationsInRange> resources = ResourceCache.
                getInstance().getAllResources();
        resources.stream().parallel().forEach(e -> {
            String rid_r = e.getResource_id();
            for (Invitation invitaion : invitations) {
                String rid_i = invitaion.getResource_id();
                if (rid_r.equals(rid_i)) {
                    e.getInvitations().add(invitaion);
                }
            }
        });
        return new ResourcesWithInvitationsInRange(fromdate, todate, resources);
    }

    /**
     * BBHelperが取り扱う全ての会議室の情報を返却します。会議の情報は含まれません。
     * 
     * @return BBHelperが取り扱う全ての会議室の情報
     */
    @RequestMapping(value = "/list",
                    method = RequestMethod.GET)
    public Collection<ResourceWithInvitationsInRange> listAllBookableResources() {
        return ResourceCache.getInstance().getAllResources();
    }

    /**
     * 指定された会議室の情報を返却します。会議の情報は含まれません。
     * 
     * @param resource_id
     * @return 指定された会議室の情報
     */
//    @RequestMapping(value = "/{resource_id}",
//                    method = RequestMethod.GET)
//    public ResourceWithInvitationsInRange getBookableResource(
//            @PathVariable("resource_id") String resource_id) {
//        return ResourceCache.getInstance().get(resource_id);
//    }

}
