package aftercoffee.org.nonsmoking365.Notice;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import aftercoffee.org.nonsmoking365.Manager.NetworkManager;
import aftercoffee.org.nonsmoking365.MyApplication;
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

        // ListItem 한개만 보이도록.
        listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (prev != -1) {
                    listView.collapseGroup(prev);
                }
                prev = groupPosition;
            }
        });

        // ListItem client test
        /*
        mAdapter.add("2015-11-12", "금연365 공지사항",
                        "금연365 공지사항입니다.\n" +
                        "금연365 공지사항입니다.\n" +
                        "금연365 공지사항입니다.\n" +
                        "금연365 공지사항입니다.\n" +
                        "금연365 공지사항입니다.");
        */

        // ListItem network test
        NetworkManager.getInstance().getNoticeData(this, new NetworkManager.OnResultListener<Notice>() {
            @Override
            public void onSuccess(Notice result) {
                for (Docs d : result.docsList) {
                    String createdDate = d.created.substring(0, d.created.indexOf("T"));
                    /* mAdapter.add(String createdDate, String title, String content) */
                    mAdapter.add(createdDate, d.title, d.content);
                }
            }

            @Override
            public void onFail(int code) {
                Toast.makeText(NoticeActivity.this, "Network error : "+code, Toast.LENGTH_SHORT).show();
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
