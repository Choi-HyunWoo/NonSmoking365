package aftercoffee.org.nonsmoking365.Manager;

import android.content.Context;

/**
 * Created by Tacademy on 2015-11-19.
 */
public class UserManager {
    private static UserManager instance;
    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }
    private UserManager() {

    }




}
