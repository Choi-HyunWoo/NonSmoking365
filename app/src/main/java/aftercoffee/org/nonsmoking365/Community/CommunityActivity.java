package aftercoffee.org.nonsmoking365.Community;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import aftercoffee.org.nonsmoking365.Community.CommunityBoardFragment;
import aftercoffee.org.nonsmoking365.Community.CommunityContentsFragment;
import android.view.MenuItem;
import android.view.View;

import aftercoffee.org.nonsmoking365.R;

public class CommunityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);
        actionBar.setTitle("금연 갤러리");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        if (savedInstanceState == null) {
            //getSupportFragmentManager().beginTransaction().add(R.id.container, new CommunityBoardFragment()).commit();
        }
    }

    public void pushCommunityBoardContentsFragment() {
        //getSupportFragmentManager().beginTransaction().replace(R.id.container, new CommunityContentsFragment())
    }


    public void popFragment() {
        getSupportFragmentManager().popBackStack();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
