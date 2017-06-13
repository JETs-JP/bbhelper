package com.oracle.poco.bbhelper;

import com.oracle.poco.bbhelper.exception.BbhelperException;
import com.oracle.poco.bbhelper.model.Invitation;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * 
 * Beehiveにある会議の情報を取り扱うためのREST APIを提供するController
 * 
 * @author hhayakaw
 */
@RestController
@RequestMapping("/api/invitations")
public class InvitationController {

    /**
     * 
     * 新たに会議を登録します
     * 
     * @param request リクエスト
     * @param committer 新たに登録する会議の情報
     * @return 登録した会議の情報
     * @throws BbhelperException
     *          他の会議と重複するなど、Beehiveの機能上許可されない会議を登録しようとした場合<br>
     *          通信失敗など、BeehiveのREST API呼出しで障害が発生した場合
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Invitation createInvitation(
            HttpServletRequest request,
            @RequestBody @Validated InvitationCommitter committer)
                    throws BbhelperException {
        Session session = (Session)request.getAttribute(Constants.REQUEST_ATTR_KEY_BBH_SESSION);
        return session.createInvitation(committer);
    }

    /**
     * 
     * 指定した時間帯に被る会議の一覧を取得します
     * 
     * @param request HTTPリクエスト
     * @param duration
     *      取得対象の会議情報の時間帯を指定するパラメータ。
     * @param floor
     *      会議の開催場所をフロアで絞り込むためのパラメータ
     * @return 該当する会議室の一覧
     * @throws BbhelperException
     *          指定されたクエリが不正な場合<br>
     *          通信失敗など、BeehiveのREST API呼出しで障害が発生した場合
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Collection<Invitation> listConflictedInvitations(
            HttpServletRequest request,
            @ModelAttribute @Validated Duration duration,
            @RequestParam(required = false) FloorCategory floor)
                    throws BbhelperException {
        Session session = (Session)request.getAttribute(Constants.REQUEST_ATTR_KEY_BBH_SESSION);
        return session.listConflictedInvitations(
                duration.getFromdate(), duration.getTodate(), floor);
    }

}
