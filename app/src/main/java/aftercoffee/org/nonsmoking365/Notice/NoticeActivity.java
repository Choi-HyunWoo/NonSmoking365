package aftercoffee.org.nonsmoking365.Notice;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import aftercoffee.org.nonsmoking365.R;

public class NoticeActivity extends AppCompatActivity {

    ExpandableListView listView;
    NoticeItemAdapter mAdapter;

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

        mAdapter.add("2015-11-12", "금연365 공지사항",
                        "금연365 공지사항입니다.\n" +
                        "금연365 공지사항입니다.\n" +
                        "금연365 공지사항입니다.\n" +
                        "금연365 공지사항입니다.\n" +
                        "금연365 공지사항입니다.");
        mAdapter.add("2015-11-12", "금연365 공지사항",
                        "금연365 공지사항입니다.\n" +
                        "금연365 공지사항입니다.\n" +
                        "금연365 공지사항입니다.\n" +
                        "금연365 공지사항입니다.\n" +
                        "금연365 공지사항입니다.");
        mAdapter.add("2015-11-12", "금연365 공지사항",
                        "금연365 공지사항입니다.\n" +
                        "금연365 공지사항입니다.\n" +
                        "금연365 공지사항입니다.\n" +
                        "금연365 공지사항입니다.\n" +
                        "금연365 공지사항입니다.");
        mAdapter.add("2015-11-12", "금연365 공지사항",
                        "금연365 공지사항입니다.\n" +
                        "금연365 공지사항입니다.\n" +
                        "금연365 공지사항입니다.\n" +
                        "금연365 공지사항입니다.\n" +
                        "금연365 공지사항입니다.");
        mAdapter.add("2015-11-12", "금연365 공지사항",
                        "금연365 공지사항입니다.\n" +
                        "금연365 공지사항입니다.\n" +
                        "금연365 공지사항입니다.\n" +
                        "금연365 공지사항입니다.\n" +
                        "금연365 공지사항입니다.");
        mAdapter.add("2015-11-12", "금연365 공지사항",
                        "금연365 공지사항입니다.\n" +
                        "금연365 공지사항입니다.\n" +
                        "금연365 공지사항입니다.\n" +
                        "금연365 공지사항입니다.\n" +
                        "금연365 공지사항입니다.");
        mAdapter.add("2015-11-12", "금연365 공지사항",
                        "금연365 공지사항입니다.\n" +
                        "금연365 공지사항입니다.\n" +
                        "금연365 공지사항입니다.\n" +
                        "금연365 공지사항입니다.\n" +
                        "금연365 공지사항입니다.");
        mAdapter.add("2015-11-12", "금연365 공지사항",
                        "금연365 공지사항입니다.\n" +
                        "금연365 공지사항입니다.\n" +
                        "금연365 공지사항입니다.\n" +
                        "금연365 공지사항입니다.\n" +
                        "금연365 공지사항입니다.");
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
