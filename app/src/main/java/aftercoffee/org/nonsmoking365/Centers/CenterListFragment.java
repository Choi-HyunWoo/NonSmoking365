package aftercoffee.org.nonsmoking365.Centers;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import aftercoffee.org.nonsmoking365.Manager.NetworkManager;
import aftercoffee.org.nonsmoking365.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CenterListFragment extends Fragment {

    ListView centerListView;
    CentersItemAdapter mAdapter;

    public CenterListFragment() {
        // Required empty public constructor
        this.setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_center_list, container, false);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("주변 보건소 및 금연 상담센터");

        centerListView = (ListView)view.findViewById(R.id.list_centers);

        // Headerview는 Listview에 adapter를 할당하기 전에 설정
        View headerView = inflater.inflate(R.layout.view_centers_header, null);
        centerListView.addHeaderView(headerView, null, false);

        mAdapter = new CentersItemAdapter();
        centerListView.setAdapter(mAdapter);

        Button btn = (Button)view.findViewById(R.id.btn_map);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapActivity.class);
                startActivity(intent);
            }
        });

        NetworkManager.getInstance().findPOI(getActivity(), new NetworkManager.OnResultListener<SearchPOIInfo>() {
            @Override
            public void onSuccess(SearchPOIInfo result) {
                for (POI poi : result.pois.poilist) {
                    mAdapter.add(poi);
                }
            }
            @Override
            public void onFail(int code) {
                Toast.makeText(getActivity(), "Network error " + code, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
