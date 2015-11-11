package aftercoffee.org.nonsmoking365.Manager;

import org.json.JSONException;
import org.json.JSONObject;

import aftercoffee.org.nonsmoking365.Data.Board;
import aftercoffee.org.nonsmoking365.JSONParsing;

/**
 * Created by Tacademy on 2015-11-11.
 */
public class BoardResult implements JSONParsing {
    Board board;

    @Override
    public void parsing(JSONObject jobject) throws JSONException {
        board = new Board();
        JSONObject jBoard = jobject.getJSONObject("board");
        board.parsing(jBoard);
    }
}
