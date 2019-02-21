package libraries.ui.chat.presenter;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by Omar on 20/12/2017.
 */

public interface ChatPresenter {
    void onCreate(Intent intent);
    void onHelloWorld(String message);
    void onStart(Activity activity);
    void onStop();
}
