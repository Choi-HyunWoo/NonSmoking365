package aftercoffee.org.nonsmoking365.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Tacademy on 2015-11-24.
 */
public class CommunityDocs {
    public String _id;
    //public int seq;
    public String writer;       // 작성자의 _id
    public String title;
    public String content;
    // int __v;                 // 몽고에서 자동으로 만들어주능거

    public User user_id;
    public List<Image> image_ids;
    public List<String> like_ids;
    public List<Comments> commentsList;
}
