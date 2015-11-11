package aftercoffee.org.nonsmoking365.Manager;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import aftercoffee.org.nonsmoking365.Manager.JSONParsing;
import aftercoffee.org.nonsmoking365.Notice.Notice;

/**
 * Created by Tacademy on 2015-11-11.
 */
public class NoticeResult implements JSONParsing {

    @SerializedName("data")
    Notice notice;

    @Override
    public void parsing(JSONObject jobject) throws JSONException {
        notice = new Notice();
        JSONObject jBoard = jobject.getJSONObject("data");
        notice.parsing(jBoard);
    }
}
