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

    public void pushBoardContentsFragment(String selectedDocID) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, BoardContentsFragment.newInstance(selectedDocID)).addToBackStack(null).commit();
    }

    public void popFragment() {
        getSupportFragmentManager().popBackStack();
    }
}