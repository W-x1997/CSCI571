package com.wx.movie.tools;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

public class SharedHelper {
    private static String NAME = "shared_data";
    private static SharedPreferences mSharedPreferences = null;


    /**
     * 单例模式
     * Singleton mode
     */

    public static synchronized SharedPreferences getInstance(Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getApplicationContext().getSharedPreferences(NAME, Context.MODE_PRIVATE);
        }
        return mSharedPreferences;
    }

    public static void putBoolean(String key, boolean value, Context context) {
        SharedHelper.getInstance(context).edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key, boolean defValue, Context context) {
        return SharedHelper.getInstance(context).getBoolean(key, defValue);
    }

    public static void putString(String key, String value, Context context) {
        SharedHelper.getInstance(context).edit().putString(key, value).apply();
    }

    public static String getString(String key, String defValue, Context context) {
        return SharedHelper.getInstance(context).getString(key, defValue);
    }

    public static void putInt(String key, int value, Context context) {
        SharedHelper.getInstance(context).edit().putInt(key, value).apply();
    }

    public static int getInt(String key, int defValue, Context context) {
        return SharedHelper.getInstance(context).getInt(key, defValue);
    }

    public static void remove(String key, Context context) {
        SharedHelper.getInstance(context).edit().remove(key).apply();
    }


    public static void clear(Context context) {
        SharedHelper.getInstance(context).edit().clear().apply();
    }

    public static Map<String,?> getAll(Context context) {
        return SharedHelper.getInstance(context).getAll();
    }

    public static boolean contains(String key, Context context) {
        return SharedHelper.getInstance(context).contains(key);
    }

}
