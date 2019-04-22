package com.example.computer.myapplication;


import android.app.Activity;
import android.content.Intent;

public class ChangeTheme {

    private static int sTheme;

    public final static int AppTheme = 0;
    public final static int MyThemeRed = 1;
    public final static int MyThemeYellow = 2;
    public final static int MyThemeChartreuse = 3;
    public final static int MyThemeCyan = 4;



    public static void changeToTheme(Activity activity, int theme) {
        sTheme = theme;
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
        activity.overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
    }


    public static void onActivityCreateSetTheme(Activity activity) {
        switch (sTheme) {

            case AppTheme:
                activity.setTheme(R.style.AppTheme);
                break;

            case MyThemeRed:
                activity.setTheme(R.style.MyThemeRed);
                break;
            case MyThemeYellow:
                activity.setTheme(R.style.MyThemeYellow);
                break;
            case MyThemeChartreuse:
                activity.setTheme(R.style.MyThemeChartreuse);
                break;
            case MyThemeCyan:
                activity.setTheme(R.style.MyThemeCyan);
                break;
        }
    }

}
