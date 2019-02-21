package libraries.ui.chat.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import libraries.MyApp;
import me.aflak.libraries.R;
import libraries.ui.chat.data.ChatModule;
import libraries.ui.chat.data.DaggerChatComponent;
import libraries.ui.chat.presenter.ChatPresenter;

/**
 * Created by Omar on 20/12/2017.
 */

public class ChatActivity extends AppCompatActivity implements ChatView{
    @BindView(R.id.activity_chat_status)
    TextView state;
    @BindView(R.id.activity_chat_messages)
    TextView messages;
    @BindView(R.id.activity_chat_hello_world)
    Button helloWorld;

    @Inject ChatPresenter presenter;

    int rpm = 0;
    int increment = 500;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        DaggerChatComponent.builder()
            .bluetoothModule(MyApp.app().bluetoothModule())
            .chatModule(new ChatModule(this))
            .build().inject(this);

        ButterKnife.bind(this);

        presenter.onCreate(getIntent());
    }

    @OnClick(R.id.activity_chat_hello_world)
    public void onHelloWorld(){
        //EditText et = (EditText)findViewById(R.id.send_value);
        //String str = et.getText().toString();
        presenter.onHelloWorld("hello world");
    }

    public void onUpButton(View v){
        rpm += increment;
        Log.d("rpm", Integer.toString(rpm));
        //TextView message = (TextView)findViewById(R.id.rpmValue);
        //message.setText(rpm);
        presenter.onHelloWorld(Integer.toString(rpm));
        Log.d("rpm", "done");
    }

    public void onDownButton(View v){
        rpm -= increment;
        //TextView message = (TextView)findViewById(R.id.rpmValue);
        //message.setText(rpm);
        presenter.onHelloWorld(Integer.toString(rpm));
    }

    @Override
    public void setStatus(String status) {
        state.setText(status);
    }

    @Override
    public void setStatus(int resId) {
        state.setText(resId);
    }

    @Override
    public void appendMessage(String message) {
        String str = messages.getText()+"\n"+message;
        messages.setText(str);
    }

    @Override
    public void enableHWButton(boolean enabled) {
        helloWorld.setEnabled(enabled);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.onStop();
    }
}
