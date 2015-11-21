package aftercoffee.org.nonsmoking365.tt.activity.notice;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import aftercoffee.org.nonsmoking365.tt.data.Notice;
import aftercoffee.org.nonsmoking365.tt.data.NoticeDocs;
import aftercoffee.org.nonsmoking365.tt.manager.NetworkManager;
import aftercoffee.org.nonsmoking365.R;

public class NoticeActivity extends AppCompatActivity {

    ExpandableListView listView;
    NoticeItemAdapter mAdapter;

    int prev = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("공지사항");
        actionBar.setElevation(0);

        listView = (ExpandableListView)findViewById(R.id.expandableListView);
        mAdapter = new NoticeItemAdapter();
        listView.setAdapter(mAdapter);

        // ListItem 클릭시 한 개만 expand되도록.
        listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (prev != -1) {
                    listView.collapseGroup(prev);
                }
                prev = groupPosition;
            }
        });

        // ListItem network
        NetworkManager.getInstance().getNoticeData(this, new NetworkManager.OnResultListener<Notice>() {
            @Override
            public void onSuccess(Notice result) {
                int count=0;
                for (NoticeDocs d : result.docsList) {
                    String createdDate = d.created.substring(0, d.created.indexOf("T"));
                    if (d.image_ids.size() != 0) {
                        /* mAdapter.add(String createdDate, String title, String content, String imageUrl) */
                        mAdapter.add(createdDate, d.title, d.content, d.image_ids.get(0).uri);
                    } else {
                        mAdapter.add(createdDate, d.title, d.content, "");
                    }
                }
            }

            @Override
            public void onFail(int code) {
                Log.d("NoticeActivity ", "network error/" + code);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
