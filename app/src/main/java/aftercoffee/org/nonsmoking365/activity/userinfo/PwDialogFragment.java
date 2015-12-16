package aftercoffee.org.nonsmoking365.activity.userinfo;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
import aftercoffee.org.nonsmoking365.manager.UserManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class PwDialogFragment extends DialogFragment {

    EditText oldPwView, newPwView, newPwCheckView;
    Button submitBtn, cancelBtn;
    String user_id;

    public PwDialogFragment() {
        // Required empty public constructor
    }

    // Dialog width, height setting
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Dialog dlg = getDialog();
        int width = getResources().getDimensionPixelSize(R.dimen.password_dialog_width);
        int height = getResources().getDimensionPixelSize(R.dimen.password_dialog_height);
        getDialog().getWindow().setLayout(width, height);
        dlg.getWindow().setLayout(width, height);
        WindowManager.LayoutParams params = dlg.getWindow().getAttributes();
        dlg.getWindow().setAttributes(params);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pw_dialog, container, false);
        oldPwView = (EditText)view.findViewById(R.id.edit_pwOld);
        newPwView = (EditText)view.findViewById(R.id.edit_pwNew);
        newPwCheckView = (EditText)view.findViewById(R.id.edit_pwNewCheck);
        submitBtn = (Button)view.findViewById(R.id.btn_submit);
        cancelBtn = (Button)view.findViewById(R.id.btn_cancel);
        user_id = UserManager.getInstance().getUser_id();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwOld = oldPwView.getText().toString();
                String pwNew = newPwView.getText().toString();
                String pwNewChcek = newPwCheckView.getText().toString();
                if (TextUtils.isEmpty(pwOld)) {
                    Toast.makeText(getActivity(), "이전 비밀번호를 입력해 주세요", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(pwNew)) {
                    Toast.makeText(getActivity(), "새 비밀번호를 입력해 주세요", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(pwNewChcek)) {
                    Toast.makeText(getActivity(), "새 비밀번호 확인 칸을 입력해 주세요", Toast.LENGTH_SHORT).show();
                } else {
                    if (pwNew.equals(pwNewChcek)) {
                        NetworkManager.getInstance().updatePassword(getActivity(), user_id, pwOld, pwNew, new NetworkManager.OnResultResponseListener<String>() {
                            @Override
                            public void onSuccess(String result) {
                                Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                                dismiss();
                            }

                            @Override
                            public void onFail(int code, String reponseString) {
                                Toast.makeText(getActivity(), reponseString, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(getActivity(), "비밀번호 확인에 입력된 비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show();
                    }
                }
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
