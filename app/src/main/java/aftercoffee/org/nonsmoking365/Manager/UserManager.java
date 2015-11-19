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

    boolean isLogined;
    int userGrade = 0;
    public static final int USER_GRADE_NORMAL = 1;      // 일반 회원
    public static final int USER_GRADE_REWARD = 2;      // 리워드 회원

    // 로그인 상태
    public void setLoginState (boolean isLogined) {
        this.isLogined = isLogined;
    }
    public boolean getLoginState() {
        return this.isLogined;
    }

    // 회원 등급
    public void setUserGrade(int userGrade) {
        this.userGrade = userGrade;
    }
    public int getUserGrade() {
        return this.userGrade;
    }
}
