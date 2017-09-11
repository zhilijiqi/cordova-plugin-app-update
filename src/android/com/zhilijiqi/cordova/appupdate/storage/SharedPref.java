package com.zhilijiqi.cordova.appupdate.storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Feng on 2017/9/8.
 */

public class SharedPref {

    private Context context;
    private String name;
    private SharedPreferences pref;

    public SharedPref(Context context,String name){
        this.context = context;
        this.name = name;
    }

    public void write(SharedPrefCallable callable){
        SharedPreferences.Editor editor = getPref(name).edit();
        callable.call(editor);
        editor.apply();
    }

    public int getInt(String key,int defValue){
        return this.getPref(name).getInt(key,defValue);
    }

    public String getString(String key,String defValue){
        return this.getPref(name).getString(key,defValue);
    }

    public boolean getBoolean(String key, boolean defValue){
        return this.getPref(name).getBoolean(key, defValue);
    }

    public SharedPreferences getPref(String name){
        return getPref(name,Context.MODE_PRIVATE);
    }

    public SharedPreferences getPref(String name, int mode){
        if(pref == null){
            pref = context.getSharedPreferences(name, mode);
        }
        return pref;
    }

    public interface SharedPrefCallable<T>{
        public T call(SharedPreferences.Editor editor);
    }
}
