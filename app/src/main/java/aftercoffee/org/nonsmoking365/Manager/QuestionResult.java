package aftercoffee.org.nonsmoking365.Manager;

import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import aftercoffee.org.nonsmoking365.Question.Question;

/**
 * Created by Tacademy on 2015-11-12.
 */
public class QuestionResult implements JSONParsing {

    @SerializedName("data")
    Question question;

    @Override
    public void parsing(JSONObject jobject) throws JSONException {
        question = new Question();
        JSONObject jQuestion = jobject.getJSONObject("data");
        question.parsing(jQuestion);
    }
}
