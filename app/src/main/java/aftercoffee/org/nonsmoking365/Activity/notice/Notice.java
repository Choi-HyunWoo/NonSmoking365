package aftercoffee.org.nonsmoking365.Activity.notice;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Tacademy on 2015-11-11.
 */
public class Notice /*implements JSONParsing*/ {
    String name;
    int count;
    int page;
    @SerializedName("docs")
    List<Docs> docsList;
}
