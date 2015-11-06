package aftercoffee.org.nonsmoking365.activity.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

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
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CountItem c = (CountItem) mAdapter.getItem(position);
                if (c.itemMode == CountItem.MODE_ON) {
                    // Dialog
                    // O, X 처리
                }
            }
        });

        return view;
    }

    // Handler로 다음 날짜를 확인.
    // 다음 날짜가 되면 버튼 하나 풀림.

    // 금연 여부 데이터 저장... (금연 성공률)

}
