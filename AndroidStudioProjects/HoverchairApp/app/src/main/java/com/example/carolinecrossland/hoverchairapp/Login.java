package com.example.carolinecrossland.hoverchairapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;

//added for login
import java.util.Map;
import java.util.HashMap;
import android.widget.EditText;

public class Login extends AppCompatActivity {
    Button loginBtn;
    EditText login_email, login_password;

    //added for login
    private Map<String, String> cred = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = findViewById(R.id.login_button);
        login_email = (EditText) findViewById(R.id.login_email);
        login_password = (EditText) findViewById(R.id.login_password);


        //what sends it to main
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public Map<String, String> addCred(String username, String password) {
        cred.put(username, password);
        return cred;
    }


}
