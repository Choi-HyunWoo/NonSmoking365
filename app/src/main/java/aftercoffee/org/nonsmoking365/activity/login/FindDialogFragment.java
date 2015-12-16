package aftercoffee.org.nonsmoking365.activity.login;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import aftercoffee.org.nonsmoking365.R;
import aftercoffee.org.nonsmoking365.manager.NetworkManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class FindDialogFragment extends DialogFragment {

    EditText findEmailView;
    Button submitBtn, cancelBtn;

    public FindDialogFragment() {
        // Required empty public constructor

    }

    // Dialog width, height setting
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Dialog dlg = getDialog();
        int width = getResources().getDimensionPixelSize(R.dimen.find_dialog_width);
        int height = getResources().getDimensionPixelSize(R.dimen.find_dialog_height);
        getDialog().getWindow().setLayout(width, height);
        dlg.getWindow().setLayout(width, height);
        WindowManager.LayoutParams params = dlg.getWindow().getAttributes();
        dlg.getWindow().setAttributes(params);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_find_dialog, container, false);
        // remove dialog title
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        // remove dialog background
        // getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        findEmailView = (EditText)view.findViewById(R.id.edit_findEmail);
        submitBtn = (Button)view.findViewById(R.id.btn_submit);
        cancelBtn = (Button)view.findViewById(R.id.btn_cancel);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkManager.getInstance().findPassword(getActivity(), findEmailView.getText().toString(), new NetworkManager.OnResultResponseListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Toast.makeText(getActivity(), "입력하신 ID Email로 인증 메일이 발송되었습니다.", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }

                    @Override
                    public void onFail(int code, String reponseString) {
                        Toast.makeText(getActivity(), "ID를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }


}
