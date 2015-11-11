package aftercoffee.org.nonsmoking365.board;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tacademy on 2015-11-11.
 */
public class Docs implements JSONParsing {
    // String id;
    int seq;
    String category;
    String title;
    String content;
    // int __v;                 // 몽고에서 자동으로 만들어주능거
    // int image_ids;
    // List<Comments> commentsList;

    @Override
    public void parsing(JSONObject jobject) throws JSONException {
        seq = jobject.getInt("seq");
        category = jobject.getString("category");
        title = jobject.getString("title");
        content = jobject.getString("content");

        /*
        commentsList = new ArrayList<Comments>();
        JSONArray array = jobject.getJSONArray("comments");
        for (int i=0; i<array.length(); i++) {
            JSONObject jComments = array.getJSONObject(i);
            Comments c = new Comments();
            c.parsing(jComments);
            commentsList.add(c);
        }
        */
    }
}
