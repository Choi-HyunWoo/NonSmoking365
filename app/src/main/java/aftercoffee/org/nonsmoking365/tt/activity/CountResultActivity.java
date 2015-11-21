package aftercoffee.org.nonsmoking365.tt.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import aftercoffee.org.nonsmoking365.tt.manager.PropertyManager;
import aftercoffee.org.nonsmoking365.R;

public class CountResultActivity extends AppCompatActivity {

    /* about Character placing */
    private static final int CHARACTER_IMG_WIDTH  = 60;     // dp
    private static final int CHARACTER_IMG_HEIGHT = 60;     // dp
    private static final int GRIDVIEW_WIDTH = 300;
    private static final int GRIDVIEW_SPACING = 1;
    private static final int GRIDVIEW_TOP_MARGIN = 100;

    /* Extra info */
    public static final int IS_SUCCESS = 1;
    public static final int IS_FAILURE = 0;

    // get from intent
    int clickPosition;
    int clickDayInfo;

    RelativeLayout layout;
    ImageView img;
    TextView titleView, infoView, resultView, messageView;
    AnimationDrawable animationDrawable;

    class Position {
        int x;
        int y;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_result);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");
        actionBar.setElevation(0);


        // get info
        Intent intent = getIntent();
        clickPosition = intent.getIntExtra("clickPosition", -1);
        clickDayInfo = intent.getIntExtra("clickDayInfo", -1);

        // View initialize
        layout = (RelativeLayout) findViewById(R.id.layout);
        titleView = (TextView)findViewById(R.id.text_title);
        infoView = (TextView)findViewById(R.id.text_info);
        resultView = (TextView)findViewById(R.id.text_result);
        messageView = (TextView)findViewById(R.id.text_message);

        // Image position initialize
        List<Position> imagePositionList = new ArrayList<Position>();
        for(int row=0 ; row<6; row++) {               // row
            for (int col = 0; col < 5; col++) {       // col
                Position position = new Position();
                // xPos
                position.x = getWindowCenterPosX()
                        - setDpToPixel(GRIDVIEW_WIDTH / 2)
                        + setDpToPixel(GRIDVIEW_SPACING)
                        + setDpToPixel(col * (CHARACTER_IMG_WIDTH + GRIDVIEW_SPACING));
                // yPos
                position.y = setDpToPixel(GRIDVIEW_TOP_MARGIN)
                        + setDpToPixel(GRIDVIEW_SPACING)
                        + setDpToPixel(row * (CHARACTER_IMG_HEIGHT + GRIDVIEW_SPACING));
                imagePositionList.add(position);
            }
        }

        // click된 position에 Image 배치
        img = new ImageView(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.leftMargin = imagePositionList.get(clickPosition).x;
        params.topMargin = imagePositionList.get(clickPosition).y;
        layout.addView(img, params);

        // 결과 보여주기
        titleView.setText((clickPosition+1)+"일차 금연 결과");
        if (clickDayInfo == IS_SUCCESS) {
            infoView.setText("성공!");
            img.setImageResource(R.drawable.ani_man_idle);
        } else {
            infoView.setTextColor(Color.RED);
            infoView.setText("실패!");
            layout.setBackgroundResource(R.color.color555555);
            img.setImageResource(R.drawable.ani_smoker_smoke);
        }
        int successCount = PropertyManager.getInstance().getCountSuccess();
        int failureCount = PropertyManager.getInstance().getCountFailure();
        int totalCount = successCount + failureCount;
        int resultPercentage = (successCount * 100) / totalCount;
        resultView.setText("현재까지\n성공 : " + successCount + "일\n실패 : " + failureCount + "일\n금연 성공률 : " + resultPercentage + "%");


        // Frame Animation set
        animationDrawable = (AnimationDrawable) img.getDrawable();
        animationDrawable.setOneShot(false);
        animationDrawable.start();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // Move character image to center
        AnimatorSet moveToCenter = new AnimatorSet();
        ObjectAnimator moveX = ObjectAnimator.ofFloat(img, "x", img.getX(), layout.getWidth()/2 - img.getWidth()/2);
        ObjectAnimator moveY = ObjectAnimator.ofFloat(img, "y", img.getY(), layout.getHeight()/2 - img.getHeight() / 2);
        moveToCenter.playTogether(moveX, moveY);
        moveToCenter.setDuration(1000);
        moveToCenter.start();

        // TextView alpha Animation
        AlphaAnimation alpha = new AlphaAnimation(0.0f, 1.0f);
        alpha.setStartOffset(1000);
        alpha.setDuration(1000);
        titleView.setAnimation(alpha);
        infoView.setAnimation(alpha);
        resultView.setAnimation(alpha);
        messageView.setAnimation(alpha);
        alpha.start();
    }

    // 클릭된 ImageView의 위치 계산 Methods
    private int getWindowCenterPosX() {             // return pixel position value
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels/2;
    }
    private int setDpToPixel(int dpValue) {
        //dpValue = 5; // margin in dips
        float d = this.getResources().getDisplayMetrics().density;
        int margin = (int) (dpValue * d); // margin in pixels
        return margin;
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
