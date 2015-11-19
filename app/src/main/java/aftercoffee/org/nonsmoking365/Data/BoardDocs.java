package aftercoffee.org.nonsmoking365.Data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import aftercoffee.org.nonsmoking365.Data.Comments;
import aftercoffee.org.nonsmoking365.Data.Image;

/**
 * Created by Tacademy on 2015-11-11.
 */
public class BoardDocs {
    public String _id;
    //public int seq;
    public String category;
    public String title;
    public String content;
    // int __v;                 // 몽고에서 자동으로 만들어주능거
    @SerializedName("image_ids")
    public List<Image> image_ids;

    public List<String> like_ids;

    @SerializedName("comments")
    public List<Comments> commentsList;

}
