package com.egemen.mapsdeneme.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.egemen.mapsdeneme.R;
import com.egemen.mapsdeneme.config.AppParameters;
import com.egemen.mapsdeneme.interfaces.ResponseHandler;
import com.egemen.mapsdeneme.model.ResponseBody;
import com.egemen.mapsdeneme.model.UserType;
import com.egemen.mapsdeneme.network.ApiManager;

import retrofit2.Call;

public class RegisterActivity extends AppCompatActivity {
    EditText firstName, lastName, username, email, password;
    Button button;
    ApiManager apm;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apm = new ApiManager();
        setContentView(R.layout.activity_register);
        firstName = findViewById(R.string.registerFirstName);
        lastName = findViewById(R.string.registerLastName);
        username = findViewById(R.string.registerUserName);
        email = findViewById(R.string.registerEmail);
        password = findViewById(R.string.registerPassword);
        button = findViewById(R.string.registerButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserType usr = new UserType(username.getText().toString(),
                        firstName.getText().toString(),
                        lastName.getText().toString(),
                        password.getText().toString(),
                        email.getText().toString());
                if(check(usr)){
                    apm.request(apm.api.postUser(usr), new ResponseHandler() {
                        @Override
                        public void onSuccess(ResponseBody data, int statusCode, String message) {
                            if(statusCode == 409){
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getApplicationContext(),"Kaydolma işleminiz başarıyla gerçekleşti!",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onFail(Call<ResponseBody> call) {

                        }
                    });
                }
            }
        });

    }

    public boolean check(UserType usr) {
        String firstName = usr.getFirstName();
        String lastName = usr.getLastName();
        String username = usr.getUsername();
        String email = usr.getEmail();
        String password = usr.getPassword();
        if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || email.isEmpty() ||
                password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Hepsini doldurun", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.length() < 4) {
            Toast.makeText(getApplicationContext(), "Sifreniz 4 karakterden kısa olamaz!", Toast.LENGTH_SHORT).show();
        }
        return true;
    }


}
