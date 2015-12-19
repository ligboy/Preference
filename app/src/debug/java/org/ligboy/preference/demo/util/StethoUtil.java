package org.ligboy.preference.demo.util;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * @author Ligboy.Liu ligboy@gmail.com.
 */
public class StethoUtil {
    /**
     * 初始化Stetho
     * Initialize Stetho
     * @param application Application
     */
    public static void initialize(Application application) {
        Stetho.initialize(
                Stetho.newInitializerBuilder(application)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(application))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(application))
                        .build());
    }
}
