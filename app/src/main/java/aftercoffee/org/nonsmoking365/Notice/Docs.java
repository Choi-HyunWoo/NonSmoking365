package aftercoffee.org.nonsmoking365.Notice;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import aftercoffee.org.nonsmoking365.Manager.JSONParsing;

/**
 * Created by Tacademy on 2015-11-11.
 */
public class Docs /*implements JSONParsing*/ {
    /**
     * 공지사항 item
     */
    String _id;
    int seq;
    String title;
    String content;
//    Boolean isAlarm;
//    int __v;
    @SerializedName("image_ids")
    List<Image> image_ids;
    String created;

}
