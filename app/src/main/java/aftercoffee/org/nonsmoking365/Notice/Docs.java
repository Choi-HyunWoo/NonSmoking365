package aftercoffee.org.nonsmoking365.Notice;

import org.json.JSONException;
import org.json.JSONObject;

import aftercoffee.org.nonsmoking365.Manager.JSONParsing;

/**
 * Created by Tacademy on 2015-11-11.
 */
public class Docs implements JSONParsing {
//    String _id;
    int seq;
    String title;
    String content;
//    Boolean isAlarm;
//    int __v;
//    int image_ids[];
    String created;

    @Override
    public void parsing(JSONObject jobject) throws JSONException {
        seq = jobject.getInt("seq");
        title = jobject.getString("title");
        content = jobject.getString("content");
        created = jobject.getString("created");
    }
}
