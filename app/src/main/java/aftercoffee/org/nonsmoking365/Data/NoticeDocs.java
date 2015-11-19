package aftercoffee.org.nonsmoking365.Data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import aftercoffee.org.nonsmoking365.Data.Image;

/**
 * Created by Tacademy on 2015-11-11.
 */
public class NoticeDocs {
    /**
     * 공지사항 item
     */
    public String _id;
    //public int seq;
    public String title;
    public String content;
    //public Boolean isAlarm;
    //public int __v;
    @SerializedName("image_ids")
    public List<Image> image_ids;
    public String created;

}
