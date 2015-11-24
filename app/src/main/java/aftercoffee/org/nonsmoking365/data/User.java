package aftercoffee.org.nonsmoking365.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Tacademy on 2015-11-18.
 */
public class User {
    public String _id;
    //public int seq;
    public String email;
    public String nick;
    public String password;
    //public int __v;
    //public String created;

    public List<Image> image_ids;
}
