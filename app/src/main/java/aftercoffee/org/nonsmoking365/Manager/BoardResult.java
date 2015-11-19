package aftercoffee.org.nonsmoking365.Manager;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import aftercoffee.org.nonsmoking365.board.Board;

/**
 * Created by Tacademy on 2015-11-11.
 */
public class BoardResult {

    @SerializedName("data")
    public Board board;

}
