package com.coreasp_sdk;

import android.util.Log;

import com.coreasp.CorePushInstanceIDService;
import com.coreasp.CorePushManager;
import com.google.firebase.iid.FirebaseInstanceId;

/**
 *  デバイストークンの更新時に呼び出されるクラス
 */
public class MyInstanceIdService extends CorePushInstanceIDService {

    /**
     * デバイストークンが更新された場合、本メソッドが呼び出される。
     *
     * トークンは次のような場合に変更されることがあります。
     *
     * - アプリによってインスタンス ID が削除される場合
     * - アプリが新しい端末で復元される場合
     * - ユーザーがアプリをアンインストール / 再インストールする場合
     * - ユーザーがアプリのデータを消去する場合
     *
     * 参考: https://firebase.google.com/docs/cloud-messaging/android/client?hl=ja
     */
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("COREASP", "Refreshed token: " + refreshedToken);

        // [ここにトークン更新時の任意の処理を記載してください]

        // CORE ASPサーバにトークンを登録する。
        CorePushManager.getInstance().registerToken();
    }

}
