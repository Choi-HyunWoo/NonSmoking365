package aftercoffee.org.nonsmoking365.activity.main;


import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import aftercoffee.org.nonsmoking365.R;
import aftercoffee.org.nonsmoking365.manager.PropertyManager;
import aftercoffee.org.nonsmoking365.manager.UserManager;

public class HealthGradeDialogFragment extends DialogFragment {

    private static final long TIME_SEC = 1000;
    private static final long TIME_MIN = 60 * TIME_SEC;
    private static final long TIME_HOUR = 60 * TIME_MIN;
    private static final long TIME_DAY = 24 * TIME_HOUR;
    private static final long TIME_YEAR = 365 * TIME_DAY;

    long startTime;
    long nonSmokingTime;

    ImageView gradeImage;
    TextView gradeInfo;

    public HealthGradeDialogFragment() {
        // Required empty public constructor
    }

    // Dialog width, height setting
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Dialog d = getDialog();
        int width = getResources().getDimensionPixelSize(R.dimen.health_dlalog_width);
        int height = getResources().getDimensionPixelSize(R.dimen.health_dlalog_height);
        getDialog().getWindow().setLayout(width, height);
        d.getWindow().setLayout(width, height);
        WindowManager.LayoutParams params = d.getWindow().getAttributes();
        d.getWindow().setAttributes(params);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startTime = PropertyManager.getInstance().getBasisStartTime();
        nonSmokingTime = System.currentTimeMillis() - startTime;
    }

    @Override
    public void onResume() {
        super.onResume();
        startTime = PropertyManager.getInstance().getBasisStartTime();
        nonSmokingTime = System.currentTimeMillis() - startTime;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_health_grade_dialog, container, false);
        // remove dialog title
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        // remove dialog background
        // getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        // View initialize
        gradeImage = (ImageView)view.findViewById(R.id.image_grade);
        gradeInfo = (TextView)view.findViewById(R.id.text_gradeInfo);
        Button btn = (Button)view.findViewById(R.id.btn_ok);

        // 건강 상태 (12단계 등급)
        if (nonSmokingTime > 15*TIME_YEAR) {
            gradeImage.setImageResource(R.drawable.icon_health_grade1);
            gradeInfo.setText(R.string.health_grade_1);
        }else if (nonSmokingTime > 10*TIME_YEAR) {
            gradeImage.setImageResource(R.drawable.icon_health_grade2);
            gradeInfo.setText(R.string.health_grade_2);
        }else if (nonSmokingTime > 5*TIME_YEAR) {
            gradeImage.setImageResource(R.drawable.icon_health_grade3);
            gradeInfo.setText(R.string.health_grade_3);
        }else if (nonSmokingTime > TIME_YEAR) {
            gradeImage.setImageResource(R.drawable.icon_health_grade4);
            gradeInfo.setText(R.string.health_grade_4);
        }else if (nonSmokingTime > 180 * TIME_DAY) {
            gradeImage.setImageResource(R.drawable.icon_health_grade5);
            gradeInfo.setText(R.string.health_grade_5);
        }else if (nonSmokingTime > 14 * TIME_DAY) {
            gradeImage.setImageResource(R.drawable.icon_health_grade6);
            gradeInfo.setText(R.string.health_grade_6);
        }else if (nonSmokingTime > 72 * TIME_HOUR) {
            gradeImage.setImageResource(R.drawable.icon_health_grade7);
            gradeInfo.setText(R.string.health_grade_7);
        }else if (nonSmokingTime > 48 * TIME_HOUR) {
            gradeImage.setImageResource(R.drawable.icon_health_grade8);
            gradeInfo.setText(R.string.health_grade_8);
        }else if (nonSmokingTime > 24 * TIME_HOUR) {
            gradeImage.setImageResource(R.drawable.icon_health_grade9);
            gradeInfo.setText(R.string.health_grade_9);
        }else if (nonSmokingTime > 8 * TIME_HOUR) {
            gradeImage.setImageResource(R.drawable.icon_health_grade10);
            gradeInfo.setText(R.string.health_grade_10);
        }else if (nonSmokingTime > 2 * TIME_HOUR) {
            gradeImage.setImageResource(R.drawable.icon_health_grade11);
            gradeInfo.setText(R.string.health_grade_11);
        }else if (nonSmokingTime > 20 * TIME_MIN) {
            gradeImage.setImageResource(R.drawable.icon_health_grade12);
            gradeInfo.setText(R.string.health_grade_12);
        }else {
            gradeImage.setImageResource(R.drawable.icon_logo_black);
            gradeInfo.setText("금연 시작 후 20분 부터 신체변화가 옵니다.\n조금만 기다려주세요");
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }


}

