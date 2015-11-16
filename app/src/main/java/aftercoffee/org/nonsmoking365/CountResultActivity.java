package aftercoffee.org.nonsmoking365;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import aftercoffee.org.nonsmoking365.Manager.PropertyManager;

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

    int clickPosition;
    int clickDayInfo;

    RelativeLayout layout;
    List<ImageView> characterList;
    ImageView img;
    TextView titleView, infoView, resultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_result);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setElevation(0);

        Intent intent = getIntent();
        clickPosition = intent.getIntExtra("clickPosition", -1);
        clickDayInfo = intent.getIntExtra("clickDayInfo", -1);

        // View init
        layout = (RelativeLayout) findViewById(R.id.layout);
        titleView = (TextView)findViewById(R.id.text_title);
        infoView = (TextView)findViewById(R.id.text_info);
        resultView = (TextView)findViewById(R.id.text_result);

        titleView.setText((clickPosition+1)+"일차 금연 결과");
        if (clickDayInfo == IS_SUCCESS) {
            infoView.setText("성공!");
        } else {
            infoView.setText("실패!");
        }

        int successCount = PropertyManager.getInstance().getCountSuccess();
        int failureCount = PropertyManager.getInstance().getCountFailure();
        int totalCount = successCount + failureCount;
        int resultPercentage = (successCount + failureCount) / totalCount * 100;
        resultView.setText("현재까지\n성공 : "+successCount+"일\n실패 : "+failureCount+"일\n금연 성공률 : "+resultPercentage+"%");


        // 캐릭터 이미지뷰를 담을 List
        // characterList = new ArrayList<ImageView>();

        // Image 배치 TEST
        for(int row=0 ; row<6; row++) {               // row
            for(int col=0; col<5; col++) {            // col
                img = new ImageView(this);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                // xPos
                params.leftMargin = getWindowCenterPosX()
                        - setDpToPixel(GRIDVIEW_WIDTH/2)
                        + setDpToPixel(GRIDVIEW_SPACING)
                        + setDpToPixel(col * (CHARACTER_IMG_WIDTH + GRIDVIEW_SPACING));
                // yPos
                params.topMargin = setDpToPixel(GRIDVIEW_TOP_MARGIN)
                        + setDpToPixel(GRIDVIEW_SPACING)
                        + setDpToPixel(row * (CHARACTER_IMG_HEIGHT + GRIDVIEW_SPACING));
                layout.addView(img, params);
                img.setImageResource(R.drawable.ani_man_idle);
            }
        }

/*
        AnimationDrawable d = (AnimationDrawable) img.getDrawable();
        d.setOneShot(false);
        d.start();
*/
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        //moveToRight(img);
        //moveViewToScreenCenter(img);
    }

    private int getWindowStatusBarHeight() {
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.layout);
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels - layout.getMeasuredHeight();
    }

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



    private void moveViewToScreenCenter(ImageView view)
    {
        //RelativeLayout root = (RelativeLayout) findViewById( R.id.layout);
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        //int statusBarOffset = dm.heightPixels - root.getMeasuredHeight();

        int originalPos[] = new int[2];
        view.getLocationOnScreen( originalPos );

        int xDest = dm.widthPixels/2 - (view.getMeasuredWidth()/2);
        int yDest = dm.heightPixels/2 - (view.getMeasuredHeight()/2);

        TranslateAnimation anim = new TranslateAnimation( 0, xDest - originalPos[0] , 0, yDest - originalPos[1] );
        anim.setDuration(1000);
        anim.setFillAfter(true);
        view.startAnimation(anim);
    }
}
