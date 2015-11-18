package aftercoffee.org.nonsmoking365.board;


import android.os.Bundle;
import android.speech.tts.TextToSpeechService;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import aftercoffee.org.nonsmoking365.Manager.LikesResult;
import aftercoffee.org.nonsmoking365.Manager.NetworkManager;
import aftercoffee.org.nonsmoking365.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BoardContentsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_DOCID = "selected_docID";

    // TODO: Rename and change types of parameters
    private String docID;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param selectedDocID Selected doc ID.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BoardContentsFragment newInstance(String selectedDocID) {
        BoardContentsFragment fragment = new BoardContentsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DOCID, selectedDocID);
        fragment.setArguments(args);
        return fragment;
    }

    DisplayImageOptions options;
    public BoardContentsFragment() {
        // Required empty public constructor
        this.setHasOptionsMenu(true);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon_image_add)
                .showImageForEmptyUri(R.drawable.icon_cigarette)
                .showImageOnFail(R.drawable.icon_cigarette)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
//                .displayer(new RoundedBitmapDisplayer(50))
                .build();
    }

    TextView titleView, contentView, categoryView;
    ImageView imageView;
    Button likeBtn;
    ListView commentListView;
    ArrayAdapter<String> mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            docID = getArguments().getString(ARG_DOCID);
        }
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("글 내용");

        // 글 내용 가져오기
        NetworkManager.getInstance().getBoardContentAndComments(getContext(), docID, new NetworkManager.OnResultListener<Docs>() {
            @Override
            public void onSuccess(Docs result) {
                titleView.setText(result.title);
                // 이미지 갯수 늘어나면 동적으로 imageview 생성하여 추가해줄 것.
                ImageLoader.getInstance().displayImage(result.image_ids.get(0).uri, imageView, options);
                contentView.setText(result.content);
                categoryView.setText(result.category);                  // 한글로 수정할것
                for (int i=0; i<result.commentsList.size(); i++) {
                    mAdapter.add(result.commentsList.get(i).content);
                }
            }

            @Override
            public void onFail(int code) {
                Log.d("BoardContent Load ", "network error/" + code);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_board_contents, container, false);
        titleView = (TextView)view.findViewById(R.id.text_title);
        imageView = (ImageView)view.findViewById(R.id.image_content);
        contentView = (TextView)view.findViewById(R.id.text_content);
        categoryView = (TextView)view.findViewById(R.id.text_category);
        commentListView = (ListView)view.findViewById(R.id.list_comment);
        likeBtn = (Button)view.findViewById(R.id.btn_like);
        mAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);
        commentListView.setAdapter(mAdapter);


        // 좋아요 버튼이 눌린 경우
        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkManager.getInstance().postBoardLike(getContext(), docID, "5642ca1d6461fe348bf67f96", new NetworkManager.OnResultListener<LikesResult>() {
                    @Override
                    public void onSuccess(LikesResult result) {
                        Toast.makeText(getActivity(), ""+result.likes, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFail(int code) {
                        Log.d("BoardContent Likes ", "network error/" + code);
                    }
                });
            }
        });


        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                ((BoardActivity)getActivity()).popFragment();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}