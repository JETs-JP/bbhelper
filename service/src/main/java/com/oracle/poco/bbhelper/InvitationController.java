package com.oracle.poco.bbhelper;

import java.time.ZonedDateTime;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.oracle.poco.bbhelper.exception.BbhelperException;
import com.oracle.poco.bbhelper.exception.BbhelperBadRequestException;
import com.oracle.poco.bbhelper.exception.ErrorDescription;
import com.oracle.poco.bbhelper.model.Invitation;

/**
 * 
 * Beehiveにある会議の情報を取り扱うためのREST APIを提供するController
 * 
 * @author hhayakaw
 */
@RestController
@RequestMapping("/invitations")
public class InvitationController {

    /**
     * 
     * 新たに会議を登録します
     * 
     * @param request リクエスト
     * @param invitation 新たに登録する会議の情報
     * @return 登録した会議の情報
     * @throws BbhelperException
     *          他の会議と重複するなど、Beehiveの機能上許可されない会議を登録しようとした場合<br>
     *          通信失敗など、BeehiveのREST API呼出しで障害が発生した場合
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Invitation createInvitation(HttpServletRequest request,
            @Valid @RequestBody Invitation invitation)
                    throws BbhelperException {
        Session session = (Session) request.getAttribute(
                Constants.REQUEST_ATTR_KEY_BBH_SESSION);
        return session.createInvitation(invitation);
    }

    /**
     * 
     * 指定した時間帯に被る会議の一覧を取得します
     * 
     * @param request HTTPリクエスト
     * @param fromdate 
     *      取得対象の会議情報の時間帯を指定するパラメータ。fromdateとtodateで
     *      指定された範囲に会議時間帯が被るものが対象となる
     * @param todate
     *      取得対象の会議情報の時間帯を指定するパラメータ。fromdateとtodateで
     *      指定された範囲に会議時間帯が被るものが対象となる
     * @param floor
     *      会議の開催場所をフロアで絞り込むためのパラメータ
     * @return 該当する会議室の一覧
     * @throws BbhelperException
     *          指定されたクエリが不正な場合<br>
     *          通信失敗など、BeehiveのREST API呼出しで障害が発生した場合
     */
    @RequestMapping(value = "/list",
                    method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Collection<Invitation> listConflictedInvitations(
            HttpServletRequest request,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            ZonedDateTime fromdate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            ZonedDateTime todate,
            @RequestParam(required = false) FloorCategory floor)
                    throws BbhelperException {
        if (fromdate.compareTo(todate) >= 0) {
            BbhelperException e = new BbhelperBadRequestException(
                    ErrorDescription.FROM_DATE_IS_LATER_THAN_TODATE,
                    HttpStatus.BAD_REQUEST);
            throw e;
        }
        Session session = (Session) request.getAttribute(
                Constants.REQUEST_ATTR_KEY_BBH_SESSION);
        return session.listConflictedInvitations(fromdate, todate, floor);
    }

}
