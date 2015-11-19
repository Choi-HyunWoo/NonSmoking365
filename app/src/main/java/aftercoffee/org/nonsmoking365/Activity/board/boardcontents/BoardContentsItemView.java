package aftercoffee.org.nonsmoking365.Activity.board.boardcontents;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import aftercoffee.org.nonsmoking365.Data.LikesResult;
import aftercoffee.org.nonsmoking365.Manager.NetworkManager;
import aftercoffee.org.nonsmoking365.Manager.UserManager;
import aftercoffee.org.nonsmoking365.R;

/**
 * Created by Tacademy on 2015-11-19.
 */
public class BoardContentsItemView extends FrameLayout {

    Context context;
    String docID;
    String user_id;

    public BoardContentsItemView(Context context, String docID) {
        super(context);
        this.context = context;
        this.docID = docID;
        user_id = UserManager.getInstance().getUser_id();
        init();
    }

    ImageView contentImageView;
    TextView titleView;
    TextView contentView;
    Button likeBtn;
    Button shareBtn;

    public void init() {
        inflate(getContext(), R.layout.view_board_contents, this);

        titleView = (TextView)findViewById(R.id.text_title);
        contentView = (TextView)findViewById(R.id.text_content);
        contentImageView = (ImageView)findViewById(R.id.image_content);
        likeBtn = (Button)findViewById(R.id.btn_like);
        shareBtn = (Button)findViewById(R.id.btn_share);

        likeBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkManager.getInstance().postBoardLike(getContext(), docID, user_id, new NetworkManager.OnResultListener<LikesResult>() {
                    @Override
                    public void onSuccess(LikesResult result) {
                        if (result.likes > 999) {
                            likeBtn.setText("좋아요 999+");
                        } else {
                            likeBtn.setText("좋아요 "+result.likes);
                        }
                    }
                    @Override
                    public void onFail(int code) {
                        Log.d("BoardContentItemView:", "network error/" + code);
                    }
                });
            }
        });

        shareBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 게시글 공유하기 구현 (Facebook)
                Toast.makeText(context, "구현중입니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setContentItem(BoardContentsItem item) {
        titleView.setText(item.title);
        contentView.setText(item.content);
        // contentImageView;
        if (item.likes > 999) {
            likeBtn.setText("좋아요 999+");
        } else {
            likeBtn.setText("좋아요 "+item.likes);
        }
    }
}