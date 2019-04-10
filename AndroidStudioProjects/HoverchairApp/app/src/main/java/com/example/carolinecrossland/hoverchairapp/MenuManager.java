package com.example.carolinecrossland.hoverchairapp;
import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;

public class MenuManager {
    public static Intent selectDrawerItem(Activity activity, MenuItem menuItem) {
        Intent intent = new Intent(activity, MainActivity.class);
        switch (menuItem.getItemId()) {
            case R.id.home:
                intent = new Intent(activity, MainActivity.class);
                break;
            case R.id.bt:
                intent = new Intent(activity, ScanActivity.class);
                break;
            case R.id.drive:
                intent = new Intent(activity, Drive.class);
                break;
            case R.id.notifications:
                intent = new Intent(activity, Notification.class);
                break;
            case R.id.logout:
                intent = new Intent(activity, Login.class);
                break;
            default:
                break;
        }
        return intent;
    }
}
