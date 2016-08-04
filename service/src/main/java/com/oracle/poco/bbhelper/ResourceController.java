package com.oracle.poco.bbhelper;

import java.time.ZonedDateTime;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oracle.poco.bbhelper.exception.BbhelperException;
import com.oracle.poco.bbhelper.log.BbhelperLogger;
import com.oracle.poco.bbhelper.model.Invitation;
import com.oracle.poco.bbhelper.model.Resource;

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
        // TODO これはパラメータから直接取りたい
        // TODO フロアが指定されなかったら、全てのフロアの情報を返却する
        FloorCategory floor = FloorCategory.fromLabel(floorCategory);
        try {
            invitations = InvitationUtils.listConflictedInvitaitons(
                    fromdate, todate, floor, context);
        } catch (BbhelperException e) {
            logger.logBbhelperException(request, e);
            throw e;
        }

        ResourcesWithInvitationsInRange retval =
                new ResourcesWithInvitationsInRange(fromdate, todate);
        for (Invitation invitation : invitations) {
            retval.addInvitation(invitation);
        }
        return retval;
    }

    /**
     * BBHelperが取り扱う全ての会議室の情報を返却します。会議の情報は含まれません。
     * 
     * @return BBHelperが取り扱う全ての会議室の情報
     */
    @RequestMapping(value = "/list",
                    method = RequestMethod.GET)
    public Collection<Resource> listAllBookableResources() {
        return ResourceCache.getInstance().getCache().values();
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
        return ResourceCache.getInstance().getResource(resource_id);
    }

}
