package aftercoffee.org.nonsmoking365.activity.main;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import aftercoffee.org.nonsmoking365.PropertyManager;
import aftercoffee.org.nonsmoking365.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class CountFragment extends Fragment {

    GridView countGridView;
    CountGridAdapter mAdapter;

    public CountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_count, container, false);

        countGridView = (GridView)view.findViewById(R.id.gridView);
        mAdapter = new CountGridAdapter();
        countGridView.setAdapter(mAdapter);

        for (int row=0; row<6; row++) {
            for (int col=0; col<5; col++) {
                int itemMode = PropertyManager.getInstance().getCountItemMode(row, col);
                mAdapter.add(itemMode);
            }
        }

        countGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                CountItem c = (CountItem) mAdapter.getItem(position);
                if (c.itemMode == CountItem.MODE_ON) {
                    // Dialog
                    // O, X 처리
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("금연 카운트");
                    builder.setMessage(position + "일차 금연에 성공하셨습니까?");

                    builder.setPositiveButton("성공", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((CountItem) mAdapter.getItem(position)).itemMode = CountItem.MODE_O;
                            PropertyManager.getInstance().setCountItemMode(position/5, position%5, CountItem.MODE_O);
                            ((CountItem) mAdapter.getItem(position + 1)).itemMode = CountItem.MODE_ON;
                            PropertyManager.getInstance().setCountItemMode(position/5, position%5+1, CountItem.MODE_ON);        // 수정 필요
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                    builder.setNegativeButton("실패", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((CountItem) mAdapter.getItem(position)).itemMode = CountItem.MODE_X;
                            ((CountItem) mAdapter.getItem(position+1)).itemMode = CountItem.MODE_ON;
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                    AlertDialog dlg = builder.create();
                    dlg.show();

                }
            }
        });

        Button btn = (Button) view.findViewById(R.id.btn_reset);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PropertyManager.getInstance().setCountItemInit();
                Toast.makeText(getActivity(), "금연 카운트가 초기화되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    // Handler로 다음 날짜를 확인.
    // 다음 날짜가 되면 버튼 하나 풀림.

    // 금연 여부 데이터 저장... (금연 성공률)

}
