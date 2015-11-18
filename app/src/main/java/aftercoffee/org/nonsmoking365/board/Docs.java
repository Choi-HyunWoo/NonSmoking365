package aftercoffee.org.nonsmoking365.board;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Tacademy on 2015-11-11.
 */
public class Docs {
    String _id;
    int seq;
    String category;
    String title;
    String content;
    // int __v;                 // 몽고에서 자동으로 만들어주능거
    @SerializedName("image_ids")
    List<Image> image_ids;

    @SerializedName("comments")
    List<Comments> commentsList;

}
