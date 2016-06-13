package com.oracle.poco.bbhelper;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.oracle.poco.bbhelper.model.Invitation;

/**
 * 
 * 予約された会議の情報のキャッシュ
 * TODO 全体において、スレッドセーフ性を考慮した記述に修正する
 * 
 * @author hhayakaw
 *
 */
class InvitationCache {

    /**
     * 本クラス唯一のインスタンス
     */
    private static InvitationCache instance = null;
    /**
     * キャッシュの本体
     */
    private List<Invitation> cache = new ArrayList<Invitation>();

    /**
     * 本クラス唯一のインスタンスを取得します。
     * 
     * @return 本クラス唯一のインスタンス
     */
    static InvitationCache getInstance() {
        if (instance == null) {
            instance = new InvitationCache();
        }
        return instance;
    }

    // TODO ディープクローンを返却しなくてよいかどうか検討する
    List<Invitation> getAll() {
        return cache;
    }

    /**
     * @param invitation
     */
    void put(Invitation invitation) {
        if (invitation == null) {
            return;
        }
        cache.add(invitation);
    }

    void put(List<Invitation> invitations) {
        if (invitations == null) {
            return; 
        }
        cache.addAll(invitations);
    }

    Collection<Invitation> listConflictedInvitaitons(
            ZonedDateTime from, ZonedDateTime to) {
        if (to == null) {
            /*
             * 「将来にわたる全て」と解釈すると、大量のスケジュールがヒットする可能性があるので、
             * この場合はエラーとする。
             */
            // TODO ログに吐く。メッセージを張決める
            throw new IllegalArgumentException();
        }
        if (from == null) {
            from = ZonedDateTime.now();
        }
        Collection<Invitation> retval = new ArrayList<Invitation>();
        for (Invitation invitation : cache) {
            /*
             *  TODO: invitaitonの日時がnullにならないように制約を入れる。ここではチェックしない
             */
            ZonedDateTime start = invitation.getStart();
            if (start.compareTo(to) >= 0 || start.isEqual(to)) {
                continue;
            }
            ZonedDateTime end = invitation.getEnd();
            if (end.compareTo(from) <= 0 || end.isEqual(from)) {
                continue;
            }
            retval.add(invitation);
        }
        return retval;
    }

    List<Invitation> listUnperpetuatedInvitaions() {
        List<Invitation> retval = new ArrayList<Invitation>();
        for (Invitation invitation : cache) {
            if (invitation.getInvitation_id() == null) {
                retval.add(invitation);
            }
        }
        return retval;
    }

}
