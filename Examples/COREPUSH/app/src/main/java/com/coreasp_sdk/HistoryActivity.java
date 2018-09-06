package com.coreasp_sdk;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.coreasp.CorePushNotificationHistoryManager;
import com.coreasp.CorePushNotificationHistoryModel;

/**
 *　通知履歴を表示するアクティビティ
 */
public class HistoryActivity extends ListActivity {

    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,  new ArrayList<String>()));
    }

    public void onResume() {
        super.onResume();

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("読み込み中...");
        progressDialog.show();

        // 通知履歴取得リクエストの実行
        CorePushNotificationHistoryManager manager = new CorePushNotificationHistoryManager(this);
        manager.requestNotificationHistory(new CorePushNotificationHistoryManager.CompletionHandler() {
            @Override
            public void notificationHistoryManagerSuccess(List<CorePushNotificationHistoryModel> notificationHistoryModelList) {
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) getListView().getAdapter();
                adapter.clear();

                for (int i = 0; i < notificationHistoryModelList.size(); i++) {
                    CorePushNotificationHistoryModel historyModel = (CorePushNotificationHistoryModel) notificationHistoryModelList.get(i);
                    String title = historyModel.getTitle();
                    String message = historyModel.getMessage();
                    String regDate = historyModel.getRegDate();

                    // 「通知タイトル : 通知メッセージ : 通知日時」の形式のテキストを設定
                    String text = title + " : " + message + " : " + regDate;
                    adapter.add(text);
                }

                adapter.notifyDataSetChanged();

                progressDialog.dismiss();
            }

            @Override
            public void notificationHistoryManagerFail() {
                progressDialog.dismiss();
            }
        });
    }

    public void onPause() {
        super.onPause();
    }

    protected void onListItemClick(ListView l, View v, int position, long id) {
        //リストアイテムをタップされた時の動作を定義
        TextView t = (TextView)v;
        super.onListItemClick(l, v, position, id);
    }
}
