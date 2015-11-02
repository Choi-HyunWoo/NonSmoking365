package aftercoffee.org.nonsmoking365;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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
    public static final String KEY_PREVIEW_CHECK = "isPreviewChecked";
    public static final String KEY_BASIS_INFO_CHECK = "isBasisInfoFilledIn";

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


    /* Check next activity */
    public void setPreviewCheck (Boolean check) {
        mEditor.putBoolean(KEY_PREVIEW_CHECK, check);
        mEditor.commit();
    }
    public Boolean getPreviewCheck() {
        return mPrefs.getBoolean(KEY_PREVIEW_CHECK, false);
    }
    public void setBasisInfoCheck (Boolean check) {
        mEditor.putBoolean(KEY_BASIS_INFO_CHECK, check);
        mEditor.commit();
    }
    public Boolean getBasisInfoCheck() {
        return mPrefs.getBoolean(KEY_BASIS_INFO_CHECK, false);
    }


    public boolean isBackupSync() {
        return mPrefs.getBoolean("perf_sync", false);
    }

}
