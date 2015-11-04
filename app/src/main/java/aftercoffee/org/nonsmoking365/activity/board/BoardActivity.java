package aftercoffee.org.nonsmoking365.activity.board;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import aftercoffee.org.nonsmoking365.R;
import aftercoffee.org.nonsmoking365.activity.ContentsActivity;

public class BoardActivity extends AppCompatActivity {

    public static final String PARAM_BOARD_TYPE = "board_type";
    public static final int TYPE_WARNING = 1;
    public static final int TYPE_TIPS = 2;

    ListView listView;
    BoardItemAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        listView = (ListView) findViewById(R.id.list_board);
        mAdapter = new BoardItemAdapter();
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BoardActivity.this, ContentsActivity.class);
                intent.putExtra("title", mAdapter.getItem(position).title);
                intent.putExtra("contents", mAdapter.getItem(position).contents);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        int boardType = intent.getIntExtra(PARAM_BOARD_TYPE, 0);
        switch (boardType) {
            case TYPE_WARNING : {
                // action bar title 변경할 것..
                Toast.makeText(BoardActivity.this, "흡연의 위험성", Toast.LENGTH_SHORT).show();
                // get warning contents data from server..

                // dummy datas...
                mAdapter.add(R.mipmap.ic_launcher, "흡연의 위험성 글 1번", "warning contents test11111111111111111111111111111111111111111111111111111111111111111");
                mAdapter.add(R.mipmap.ic_launcher, "흡연의 위험성 글 2번", "warning contents test22222222222222222222222222222222222222222222222222222222222222222222222222");
                mAdapter.add(R.mipmap.ic_launcher, "흡연의 위험성 글 3번", "warning contents test33333333333333333333333333333333333333333333333333333333333333333333333");

                break;
            }
            case TYPE_TIPS : {
                // action bar title 변경할 것..
                Toast.makeText(BoardActivity.this, "금연 팁", Toast.LENGTH_SHORT).show();
                // get tips contents data from server..

                // dummy datas...
                mAdapter.add(R.mipmap.ic_launcher, "금연 팁 글 1번", "tips contents test! 11111111111111111111111 tips contents test! contents test! contents test! contents test!");
                mAdapter.add(R.mipmap.ic_launcher, "금연 팁 글 2번", "tips contents test! 2222222222222222222222222 tips contents test! contents test! contents test! contents test!");
                mAdapter.add(R.mipmap.ic_launcher, "금연 팁 글 3번", "tips contents test! 333333333333333333 tips contents test! contents test! contents test! contents test!");

                break;
            }
            default: {
                // error...
                Toast.makeText(BoardActivity.this, "데이터를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
