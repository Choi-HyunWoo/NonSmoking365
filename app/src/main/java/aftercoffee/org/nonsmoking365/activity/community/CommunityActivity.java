package aftercoffee.org.nonsmoking365.activity.community;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import aftercoffee.org.nonsmoking365.R;
import aftercoffee.org.nonsmoking365.activity.community.communitycontents.CommunityContentsFragment;
import aftercoffee.org.nonsmoking365.activity.community.communitylist.CommunityBoardFragment;
import aftercoffee.org.nonsmoking365.activity.login.LoginActivity;
import aftercoffee.org.nonsmoking365.manager.UserManager;

public class CommunityActivity extends AppCompatActivity {

    public static FloatingActionButton fab;
    boolean isLogined;

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

        isLogined = UserManager.getInstance().getLoginState();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLogined) {
                    Intent intent = new Intent(CommunityActivity.this, CommunityPostActivity.class);
                    startActivity(intent);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CommunityActivity.this);
                    builder.setIcon(R.drawable.icon_logo_black);
                    builder.setTitle("로그인");
                    builder.setMessage("글 작성은 회원만 가능합니다\n로그인 페이지로 이동하시겠습니까?");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(CommunityActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    AlertDialog dlg = builder.create();
                    dlg.show();
                }
            }
        });


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new CommunityBoardFragment()).commit();
        }
    }

    public void pushCommunityContentsFragment(String selectedDocID) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, CommunityContentsFragment.newInstance(selectedDocID)).addToBackStack(null).commit();
    }
    public void popFragment() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isLogined = UserManager.getInstance().getLoginState();
    }
}
