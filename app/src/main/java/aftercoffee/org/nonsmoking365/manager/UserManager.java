package aftercoffee.org.nonsmoking365.manager;

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

    // Login state
    boolean isLogined;
    public void setLoginState (boolean isLogined) {
        this.isLogined = isLogined;
    }
    public boolean getLoginState() {
        return this.isLogined;
    }

    // User grade
    public static final int USER_GRADE_NONE = 0;        // 비회원
    public static final int USER_GRADE_NORMAL = 1;      // 일반 회원
    public static final int USER_GRADE_REWARD = 2;      // 리워드 회원
    int userGrade = USER_GRADE_NONE;

    public void setUserGrade(int userGrade) {
        this.userGrade = userGrade;
    }
    public int getUserGrade() {
        return this.userGrade;
    }

    public static final String USER_GRADE_NONE_STRING = "비회원";        // 비회원
    public static final String USER_GRADE_NORMAL_STRING = "일반 회원";      // 일반 회원
    public static final String USER_GRADE_REWARD_STRING = "리워드 회원";      // 리워드 회원
    String userGradeToString = USER_GRADE_NONE_STRING;
    public String getUserGradeToString() {
        switch (this.userGrade) {
            case USER_GRADE_NORMAL:
                return USER_GRADE_NORMAL_STRING;
            case USER_GRADE_REWARD:
                return USER_GRADE_REWARD_STRING;
            default:
                return USER_GRADE_NONE_STRING;
        }
    }

    // User info
    String _id;
    String nickname;
    String email;
    String password;
    String profileImageURL;

    public void setUser_id(String _id) {
        this._id = _id;
    }
    public String getUser_id() {
        return this._id;
    }
    public void setUserNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getUserNickname() {
        return this.nickname;
    }
    public void setUserEmail(String email) {
        this.email = email;
    }
    public String getUserEmail() {
        return this.email;
    }
    public void setUserPassword(String password) {
        this.password = password;
    }
    public String getUserPassword() {
        return this.password;
    }
    public void setUserProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }
    public String getUserProfileImageURL() {
        return this.profileImageURL;
    }

}
