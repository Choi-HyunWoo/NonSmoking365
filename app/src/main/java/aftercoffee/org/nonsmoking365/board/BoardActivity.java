package aftercoffee.org.nonsmoking365.board;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import aftercoffee.org.nonsmoking365.R;

public class BoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);
        actionBar.setTitle("금연 정보");

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new BoardFragment()).commit();
        }
    }

    public void pushBoardContentsFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new BoardContentsFragment()).addToBackStack(null).commit();
    }

    public void pushBoardCommentsFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new BoardCommentsFragment()).addToBackStack(null).commit();
    }

    public void popFragment() {
        getSupportFragmentManager().popBackStack();
    }
}

/*
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

        mAdapter.add(R.mipmap.ic_launcher, "흡연의 위험성 글 1번", "warning contents test11111111111111111111111111111111111111111111111111111111111111111");
        mAdapter.add(R.mipmap.ic_launcher, "금연팁 글 2번", "warning contents test22222222222222222222222222222222222222222222222222222222222222222222222222");
        mAdapter.add(R.mipmap.ic_launcher, "광고 글 3번", "warning contents test33333333333333333333333333333333333333333333333333333333333333333333333");

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
*/