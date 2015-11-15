package aftercoffee.org.nonsmoking365;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.opengl.EGLConfig;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class CountResultActivity extends AppCompatActivity {

    RelativeLayout layoutBase;
    List<ImageView> characterList;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_result);
        layoutBase = (RelativeLayout) findViewById(R.id.layout);

        // 캐릭터 이미지뷰를 담을 List
        // characterList = new ArrayList<ImageView>();
        img = new ImageView(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.leftMargin = 200;
        params.topMargin = 200;
        layoutBase.addView(img, params);

        img.setImageResource(R.drawable.ani_man_idle);

        AnimationDrawable d = (AnimationDrawable) img.getDrawable();
        d.setOneShot(false);
        d.start();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        //moveToRight(img);
        moveViewToScreenCenter(img);
    }

    private void moveToRight(ImageView view)
    {
        TranslateAnimation anim = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 3f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        anim.setDuration(3000);
        anim.setFillAfter(true);
        anim.setRepeatMode(Animation.RESTART);
        view.startAnimation(anim);
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
