package aftercoffee.org.nonsmoking365.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import aftercoffee.org.nonsmoking365.R;

public class CommentsActivity extends AppCompatActivity {

    TextView titleView;
    ListView listView;
    ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        titleView = (TextView)findViewById(R.id.text_title);
        listView = (ListView)findViewById(R.id.list_comments);

        Intent data = getIntent();
        String title = data.getStringExtra("title");
        titleView.setText(title);

        // title > 글을 서버에서 찾아서 해당 글에 달린 댓글들 list로 불러오기
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(mAdapter);
        for (int i=0; i<10; i++) {
            mAdapter.add("댓글 "+(i+1));
        }

    }
}
