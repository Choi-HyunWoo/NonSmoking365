package aftercoffee.org.nonsmoking365.data;

/**
 * Created by Tacademy on 2015-11-11.
 */
public class Comments {
    public String _id;
    //public int seq;
    //public String user_id;
    public String content;
    public String created;
    public User user_id;

    @Override
    public String toString() {
        return content;
    }
}
