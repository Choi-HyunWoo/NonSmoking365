package aftercoffee.org.nonsmoking365;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Date;

import aftercoffee.org.nonsmoking365.activity.main.CountItem;

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

    public static final String KEY_ID = "id";
    public static final String KEY_PASSWORD = "password";

    /* Launch check Keys */
    public static final String KEY_LAUNCH_PREVIEW_CHECK = "isPreviewChecked";
    public static final String KEY_LAUNCH_BASISINFO_CHECK = "isBasisInfoFilledIn";

    /* Basis info keys */
    public static final String KEY_BASISINFO_MOTTO = "basisInfo_motto";
    public static final String KEY_BASISINFO_START_DATE = "basisInfo_startDate";
    public static final String KEY_BASISINFO_NUM_OF_CIGAR = "basisInfo_numOfCigar";
    public static final String KEY_BASISINFO_PACK_PRICE = "basisInfo_packPrice";
    public static final String KEY_BASISINFO_GENDER = "basisInfo_gender";
    public static final String KEY_BASISINFO_BIRTH_DATE = "basisInfo_birthDate";

    /* CountItem mode keys */
    public static final String KEY_COUNT_ITEM_POSITION = "countitem_position:";         // + position
    public static final String KEY_COUNT_START_TIME = "count_start_time";
    public static final String KEY_COUNT_TODAY_POS = "today_count_position";


    public void setId(String id) {
        mEditor.putString(KEY_ID, id);
        mEditor.commit();
    }
    public String getId() {
        return mPrefs.getString(KEY_ID, "");
    }

    public void setPassword(String password) {
        mEditor.putString(KEY_PASSWORD, password);
        mEditor.commit();
    }
    public String getPassword() {
        return mPrefs.getString(KEY_PASSWORD, "");
    }


    /* Choice next activity */
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


    /* Basis info set */
    public void setBasisMotto(String motto) {
        mEditor.putString(KEY_BASISINFO_MOTTO, motto);
        mEditor.commit();
    }
    public void setBasisStartDate(String startDate) {
        mEditor.putString(KEY_BASISINFO_START_DATE, startDate);
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

    /* get BasisInfo */
    public String getBasisMotto() {
        return mPrefs.getString(KEY_BASISINFO_MOTTO, "금연 목표를 입력해 주세요");
    }
    public String getBasisStartDate() {
        return mPrefs.getString(KEY_BASISINFO_START_DATE, "");
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
        mEditor.putInt(KEY_COUNT_ITEM_POSITION+position, mode);
        mEditor.commit();
    }
    public void setCountStartTime(long countStartTime) {
        mEditor.putLong(KEY_COUNT_START_TIME, countStartTime);
        mEditor.commit();
    }

    /* get CountItem mode */
    public int getCountItemMode(int position) {
        return mPrefs.getInt(KEY_COUNT_ITEM_POSITION+position, 0);
    }
    public Long getCountStartTime() {
        return mPrefs.getLong(KEY_COUNT_START_TIME, 0L);
    }

    public boolean isBackupSync() {
        return mPrefs.getBoolean("perf_sync", false);
    }

}
