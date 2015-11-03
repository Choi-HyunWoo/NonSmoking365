package aftercoffee.org.nonsmoking365.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import aftercoffee.org.nonsmoking365.R;
import aftercoffee.org.nonsmoking365.adapter.BoardItemAdapter;

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

        Intent intent = getIntent();
        int boardType = intent.getIntExtra(PARAM_BOARD_TYPE, 0);
        switch (boardType) {
            case TYPE_WARNING : {
                // action bar title 변경할 것..
                Toast.makeText(BoardActivity.this, "흡연의 위험성", Toast.LENGTH_SHORT).show();
                // get warning contents data from server..

                // dummy datas...
                mAdapter.add(R.mipmap.ic_launcher, "warning title test1", "warning contents test! contents test! contents test! contents test! contents test! contents test! contents test!");
                mAdapter.add(R.mipmap.ic_launcher, "warning title test2", "warning contents test! contents test! contents test! contents test! contents test! contents test! contents test!");
                mAdapter.add(R.mipmap.ic_launcher, "warning title test3", "warning contents test! contents test! contents test! contents test! contents test! contents test! contents test!");

                break;
            }
            case TYPE_TIPS : {
                // action bar title 변경할 것..
                Toast.makeText(BoardActivity.this, "금연 팁", Toast.LENGTH_SHORT).show();
                // get tips contents data from server..

                // dummy datas...
                mAdapter.add(R.mipmap.ic_launcher, "tips title test1", "tips contents test! contents test! contents test! contents test! contents test! contents test! contents test!");
                mAdapter.add(R.mipmap.ic_launcher, "tips title test2", "tips contents test! contents test! contents test! contents test! contents test! contents test! contents test!");
                mAdapter.add(R.mipmap.ic_launcher, "tips title test3", "tips contents test! contents test! contents test! contents test! contents test! contents test! contents test!");

                break;
            }
            default: {
                // error...
                Toast.makeText(BoardActivity.this, "데이터를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
