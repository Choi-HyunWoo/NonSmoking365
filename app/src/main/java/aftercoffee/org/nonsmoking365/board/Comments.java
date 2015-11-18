package aftercoffee.org.nonsmoking365.board;

/**
 * Created by Tacademy on 2015-11-11.
 */
public class Comments{
    String _id;
    int seq;
    String content;
    String user_id;
    String created;

    @Override
    public String toString() {
        return content;
    }
}
