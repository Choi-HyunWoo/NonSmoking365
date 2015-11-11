package aftercoffee.org.nonsmoking365.board;

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
public class Board implements JSONParsing {
    String name;
    int count;
    int page;
    @SerializedName("docs")
    List<Docs> docsList;

    @Override
    public void parsing(JSONObject jobject) throws JSONException {
        name = jobject.getString("name");
        count = jobject.getInt("count");
        page = jobject.getInt("page");

        docsList = new ArrayList<Docs>();
        JSONArray array = jobject.getJSONArray("docs");
        for (int i=0; i<array.length(); i++) {
            JSONObject jDocs = array.getJSONObject(i);
            Docs d = new Docs();
            d.parsing(jDocs);
            docsList.add(d);
        }
    }
}
