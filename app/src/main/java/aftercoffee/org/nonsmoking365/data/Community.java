package aftercoffee.org.nonsmoking365.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Tacademy on 2015-11-24.
 */
public class Community {
    public String name;
    public int count;
    public int page;
    @SerializedName("docs")
    public List<CommunityDocs> docsList;

}
