package org.ligboy.preference.demo;

import android.app.Application;

import org.ligboy.preference.demo.util.StethoUtil;

/**
 * @author Ligboy.Liu ligboy@gmail.com.
 */
public class PreferenceApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        StethoUtil.initialize(this);
    }
}
