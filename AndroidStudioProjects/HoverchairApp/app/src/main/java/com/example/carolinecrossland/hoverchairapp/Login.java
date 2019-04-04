package com.example.carolinecrossland.hoverchairapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.content.Intent;

//added for login
import java.util.Map;
import java.util.HashMap;
import android.widget.EditText;

public class Login extends AppCompatActivity {
    Button loginBtn;
    Button createBtn;
    EditText login_email, login_password;

    //added for login
    private Map<String, String> cred = new HashMap<String, String>() {{
        put("Admin", "pass");
    }};
    String emailText, passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = findViewById(R.id.login_button);
        createBtn = findViewById(R.id.create_button);
        login_email = (EditText)findViewById(R.id.login_email);
        login_password = (EditText)findViewById(R.id.login_password);

        //what sends it to main
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailText = login_email.getText().toString();
                passwordText = login_password.getText().toString();
                if(cred.containsKey(emailText))
                {
                    if(passwordText.equals(cred.get(emailText))){
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailText = login_email.getText().toString();
                passwordText = login_password.getText().toString();
                if(!cred.containsKey(emailText)){
                    addCred(emailText, passwordText);
                    login_email.getText().clear();
                    login_password.getText().clear();
                }
            }
        });

    }

    public Map<String, String> addCred(String username, String password) {
        cred.put(username, password);
        return cred;
    }


}
