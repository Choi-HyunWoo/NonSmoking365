package aftercoffee.org.nonsmoking365.Manager;

import com.google.gson.annotations.SerializedName;

import aftercoffee.org.nonsmoking365.Notice.Notice;

/**
 * Created by Tacademy on 2015-11-11.
 */
public class NoticeResult /*implements JSONParsing*/ {
    @SerializedName("data")
    Notice notice;
}
