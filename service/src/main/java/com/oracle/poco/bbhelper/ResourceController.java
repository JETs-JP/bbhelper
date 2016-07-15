package com.oracle.poco.bbhelper;

import java.time.ZonedDateTime;
import java.util.Collection;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oracle.poco.bbhelper.model.ResourceWithInvitationsInRange;
import com.oracle.poco.bbhelper.utilities.BbhelperLogger;

import com.oracle.poco.bbhelper.model.ResourcesWithInvitationsInRange;
import com.oracle.poco.bbhelper.exception.BbhelperException;
import com.oracle.poco.bbhelper.model.Invitation;

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
            @RequestHeader(SessionPool.HEADER_KEY_BBH_AUTHORIZED_SESSION)
            String session_id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            ZonedDateTime fromdate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            ZonedDateTime todate) throws BbhelperException {
        TimeoutManagedContext context =
               SessionPool.getInstance().get(session_id);
        Collection<Invitation> invitations = InvitationUtils.
                listConflictedInvitaitons(fromdate, todate, context);
        Collection<ResourceWithInvitationsInRange> resources = ResourceCache.
                getInstance().getAllResources();
        // TODO ロジックの見直し
        for (ResourceWithInvitationsInRange resource : resources) {
            String rid_r = resource.getResource_id();
            for (Invitation invitaion : invitations) {
                String rid_i = invitaion.getResource_id();
                if (rid_i != null && rid_i.equals(rid_r)) {
                    resource.getInvitations().add(invitaion);
                }
            }
        }
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
    @RequestMapping(value = "/{resource_id}",
                    method = RequestMethod.GET)
    public ResourceWithInvitationsInRange getBookableResource(
            @PathVariable("resource_id") String resource_id) {
        return ResourceCache.getInstance().get(resource_id);
    }

    /**
     * シンプルな文字列を返却します。サーバーの稼働を確認する目的で設けられたAPIです。
     * 
     * @return 文字列 "I'm working..."
     */
    @RequestMapping(value = "/ping",
                    method = RequestMethod.GET)
    public String ping() {
        return "I'm working...";
    }

}
