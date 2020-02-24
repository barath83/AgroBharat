package com.example.agrobharat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    Button login;
    TextView username,password;
    EditText getusername,getpassword;
    static String langcheck,username_handle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (Button)findViewById(R.id.loginbutton);
        username = (TextView)findViewById(R.id.username);
        password = (TextView) findViewById(R.id.password);
        getusername = (EditText)findViewById(R.id.usernamebox);
        getpassword = (EditText) findViewById(R.id.passwordbox);



        langcheck = MainActivity.language;

        switch (langcheck) {
            case "tamil" :
                username.setText(getResources().getString(R.string.login_tamil_username));
                password.setText(getResources().getString(R.string.login_tamil_password));
                login.setText(getResources().getString(R.string.login_tamil));
                break;

            case "telugu" :
                username.setText(getResources().getString(R.string.login_telugu_username));
                password.setText(getResources().getString(R.string.login_telugu_password));
                login.setText(getResources().getString(R.string.login_telugu));
                break;

            case "hindi" :
                username.setText(getResources().getString(R.string.login_hindi_username));
                password.setText(getResources().getString(R.string.login_hindi_password));
                login.setText(getResources().getString(R.string.login_hindi));
                break;

            case "english" :
                username.setText(getResources().getString(R.string.login_english_username));
                password.setText(getResources().getString(R.string.login_english_password));
                login.setText(getResources().getString(R.string.login_english));
                break;
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username_handle = getusername.getText().toString();

                Intent intent = new Intent(LoginActivity.this,NewsWall.class);
                startActivity(intent);

            }
        });


    }
}
