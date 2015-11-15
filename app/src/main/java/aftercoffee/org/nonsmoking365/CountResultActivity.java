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
import android.view.View;
import android.view.ViewGroup;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_result);
        layoutBase = (RelativeLayout) findViewById(R.id.layout);

        // 캐릭터 이미지뷰를 담을 List
        characterList = new ArrayList<ImageView>();

        ImageView img = new ImageView(this);
        img.setImageResource(R.drawable.ani_man_idle);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.leftMargin = 100;
        params.topMargin = 100;

        img.setLayoutParams(params);
        layoutBase.addView(img);

        AnimationDrawable d = (AnimationDrawable) img.getDrawable();
        d.start();

        //TranslateAnimation transAnimation = new TranslateAnimation(img.getX(), 20, img.getY(), 20);

    }

}
