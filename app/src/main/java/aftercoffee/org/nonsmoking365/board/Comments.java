package aftercoffee.org.nonsmoking365.board;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tacademy on 2015-11-11.
 */
public class Comments implements JSONParsing{
    // String _id;
    int seq;
    String content;
    String user_id;
    String created;

    @Override
    public void parsing(JSONObject jobject) throws JSONException {
        seq = jobject.getInt("seq");
        content = jobject.getString("content");
        user_id = jobject.getString("user_id");         // email이나 닉네임으로 변경..
        created = jobject.getString("created");
    }

    @Override
    public String toString() {
        return content;
    }
}
