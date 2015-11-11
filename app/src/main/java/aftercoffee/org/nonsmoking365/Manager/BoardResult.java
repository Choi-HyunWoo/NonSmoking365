package aftercoffee.org.nonsmoking365.Manager;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import aftercoffee.org.nonsmoking365.board.Board;

/**
 * Created by Tacademy on 2015-11-11.
 */
public class BoardResult implements JSONParsing {

    @SerializedName("data")
    Board board;

    @Override
    public void parsing(JSONObject jobject) throws JSONException {
        board = new Board();
        JSONObject jBoard = jobject.getJSONObject("data");
        board.parsing(jBoard);
    }
}
