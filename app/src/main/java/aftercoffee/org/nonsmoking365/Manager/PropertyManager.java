package aftercoffee.org.nonsmoking365.Manager;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import aftercoffee.org.nonsmoking365.Activity.main.CountItem;
import aftercoffee.org.nonsmoking365.MyApplication;

/**
 * Created by Tacademy on 2015-11-02.
 */
public class PropertyManager {                  // 설정값 Manager (Singleton)
    private static PropertyManager instance;
    public static PropertyManager getInstance() {
        if (instance == null) {
            instance = new PropertyManager();
        }
        return instance;
    }

    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;           // 값을 저장할 때는 Editor

    private PropertyManager() {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        mEditor = mPrefs.edit();
    }

    /* GCM Token */
    private static final String REG_ID = "regToken";
    public static final String GCM_SENDED = "gcm_sended";

    /* Auto Login Checked */
    public static final String KEY_AUTO_LOGIN = "auto_login";
    public static final String KEY_AUTO_ID = "auto_id";
    public static final String KEY_AUTO_PASSWORD = "auto_password";

    /* Launch check Keys */
    public static final String KEY_LAUNCH_PREVIEW_CHECK = "isPreviewChecked";
    public static final String KEY_LAUNCH_BASISINFO_CHECK = "isBasisInfoFilledIn";

    /* Basis info keys */
    public static final String KEY_BASISINFO_MOTTO = "basisInfo_motto";
    public static final String KEY_BASISINFO_START_TIME = "basisInfo_startTime";
    public static final String KEY_BASISINFO_NUM_OF_CIGAR = "basisInfo_numOfCigar";
    public static final String KEY_BASISINFO_PACK_PRICE = "basisInfo_packPrice";
    public static final String KEY_BASISINFO_GENDER = "basisInfo_gender";
    public static final String KEY_BASISINFO_BIRTH_DATE = "basisInfo_birthDate";

    /* CountItem mode keys */
    public static final String KEY_COUNT_ITEM_POSITION = "countitem_position:";         // + position
    public static final String KEY_COUNT_START_TIME = "count_start_time";
    public static final String KEY_COUNT_SUCCESS = "count_success";
    public static final String KEY_COUNT_FAILURE = "count_failure";


    // GCM token
    public void setRegistrationToken(String regId) {
        mEditor.putString(REG_ID, regId);
        mEditor.commit();
    }
    public String getRegistrationToken() {
        return mPrefs.getString(REG_ID, "");
    }
    // GCM sended check
    public void setGCMSended(boolean isSended) {
        mEditor.putBoolean(GCM_SENDED, isSended);
        mEditor.commit();
    }
    public boolean getGCMSended() {
        return mPrefs.getBoolean(GCM_SENDED, false);
    }


    // 자동 로그인 설정
    public void setAutoLogin(boolean isAutoLogin) {
        mEditor.putBoolean(KEY_AUTO_LOGIN, isAutoLogin);
        mEditor.commit();
    }
    public boolean getAutoLogin() {
        return mPrefs.getBoolean(KEY_AUTO_LOGIN, false);
    }
    public void setAutoLoginId(String id) {
        mEditor.putString(KEY_AUTO_ID, id);
        mEditor.commit();
    }
    public String getAutoLoginId() {
        return mPrefs.getString(KEY_AUTO_ID, "");
    }

    public void setAutoLoginPassword(String password) {
        mEditor.putString(KEY_AUTO_PASSWORD, password);
        mEditor.commit();
    }
    public String getAutoLoginPassword() {
        return mPrefs.getString(KEY_AUTO_PASSWORD, "");
    }


    /* Information about when the Application is first launched */
    public void setPreviewCheck (Boolean check) {
        mEditor.putBoolean(KEY_LAUNCH_PREVIEW_CHECK, check);
        mEditor.commit();
    }
    public Boolean getPreviewCheck() {
        return mPrefs.getBoolean(KEY_LAUNCH_PREVIEW_CHECK, false);
    }
    public void setBasisInfoCheck (Boolean check) {
        mEditor.putBoolean(KEY_LAUNCH_BASISINFO_CHECK, check);
        mEditor.commit();
    }
    public Boolean getBasisInfoCheck() {
        return mPrefs.getBoolean(KEY_LAUNCH_BASISINFO_CHECK, false);
    }


    /* Basis info setting */
    public void setBasisMotto(String motto) {
        mEditor.putString(KEY_BASISINFO_MOTTO, motto);
        mEditor.commit();
    }
    public void setBasisStartTime(long startTime) {
        mEditor.putLong(KEY_BASISINFO_START_TIME, startTime);
        mEditor.commit();
    }
    public void setBasisNumOfCigar(String numOfCigar) {
        mEditor.putString(KEY_BASISINFO_NUM_OF_CIGAR, numOfCigar);
        mEditor.commit();
    }
    public void setBasisPackPrice(String packPrice) {
        mEditor.putString(KEY_BASISINFO_PACK_PRICE, packPrice);
        mEditor.commit();
    }
    public void setBasisGender(String gender) {
        mEditor.putString(KEY_BASISINFO_GENDER, gender);
        mEditor.commit();
    }
    public void setBasisBirthDate(String birthDate) {
        mEditor.putString(KEY_BASISINFO_BIRTH_DATE, birthDate);
        mEditor.commit();
    }


    /* get Basis info */
    public String getBasisMotto() {
        return mPrefs.getString(KEY_BASISINFO_MOTTO, "금연 목표를 입력해 주세요");
    }
    public long getBasisStartTime() {
        return mPrefs.getLong(KEY_BASISINFO_START_TIME, 0);
    }
    public String getBasisNumOfCigar() {
        return mPrefs.getString(KEY_BASISINFO_NUM_OF_CIGAR, "");
    }
    public String getBasisPackPrice() {
        return mPrefs.getString(KEY_BASISINFO_PACK_PRICE, "");
    }
    public String getBasisGender() {
        return mPrefs.getString(KEY_BASISINFO_GENDER, "");
    }
    public String getBasisBirthDate() {
        return mPrefs.getString(KEY_BASISINFO_BIRTH_DATE, "");
    }


    /* CountItems Reset */
    public void setCountItemReset() {
        setCountItemMode(0, CountItem.MODE_ON);
        for (int i=1; i<30; i++)
            setCountItemMode(i, CountItem.MODE_OFF);
    }

    /* set CountItem mode */
    public void setCountItemMode(int position, int mode) {        // 행, 열
        mEditor.putInt(KEY_COUNT_ITEM_POSITION + position, mode);
        mEditor.commit();
    }
    public void setCountStartTime(long countStartTime) {
        mEditor.putLong(KEY_COUNT_START_TIME, countStartTime);
        mEditor.commit();
    }

    /* get CountItem mode */
    public int getCountItemMode(int position) {
        return mPrefs.getInt(KEY_COUNT_ITEM_POSITION + position, 0);
    }
    public Long getCountStartTime() {
        return mPrefs.getLong(KEY_COUNT_START_TIME, -1);
    }

    /* set Count info */
    public void setCountSuccess(int successCount) {
        mEditor.putInt(KEY_COUNT_SUCCESS, successCount);
        mEditor.commit();
    }
    public void setCountFailure(int failureCount) {
        mEditor.putInt(KEY_COUNT_FAILURE, failureCount);
        mEditor.commit();
    }

    /* get Count info */
    public int getCountSuccess() {
        return mPrefs.getInt(KEY_COUNT_SUCCESS, 0);
    }
    public int getCountFailure() {
        return mPrefs.getInt(KEY_COUNT_FAILURE, 0);
    }


    //..
    public boolean isBackupSync() {
        return mPrefs.getBoolean("perf_sync", false);
    }

}
