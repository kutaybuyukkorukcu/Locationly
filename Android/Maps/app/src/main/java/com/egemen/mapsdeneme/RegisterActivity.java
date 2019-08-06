package com.egemen.mapsdeneme;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    EditText isim,soyisim,regusername,emailadress,pass;
    Button button;
    User user;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        isim=(EditText)findViewById(R.string.kayitname);
        soyisim=(EditText)findViewById(R.string.kayitlastname);
        regusername=(EditText)findViewById(R.string.kayitusername);
        emailadress=(EditText)findViewById(R.string.kayitemail);
        pass=(EditText)findViewById(R.string.kayitpass);
        button=(Button)findViewById(R.string.kayitbuton);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kayıt();
            }
        });

    }
    public void kayıt(){
        if( !onay()){
            onFail();
        }else{
            saveServer();
            Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
        }


    }

    public void onFail(){
        Toast.makeText(getBaseContext(), "fail", Toast.LENGTH_SHORT).show();

    }
    private void saveServer(){
        ArrayList<User> list=new ArrayList<User>();
        ArrayList<String> a=new ArrayList<String>();
        User user=new User(regusername.getText().toString(),isim.getText().toString(),soyisim.getText().toString(),pass.getText().toString(),emailadress.getText().toString());
        list.add(user);

        System.out.println(":::::"+user.getEmail()+user.getPassword());
    }
    public boolean onay(){
        boolean kontrol=true;
        String name = isim.getText().toString();
        String lastname = soyisim.getText().toString();
        String username=regusername.getText().toString();
        String email = emailadress.getText().toString();
        String password = pass.getText().toString();
        if(name.isEmpty() || lastname.isEmpty() || username.isEmpty() || email.isEmpty() ||
                password.isEmpty() ){
            Toast.makeText(getApplicationContext(), "Hepsini doldurun", Toast.LENGTH_SHORT).show();
            kontrol=false;
        }
        else if(password.length() <4){
            Toast.makeText(getApplicationContext(),"Sifreniz 4 karakterden kısa olamaz!",Toast.LENGTH_SHORT).show();
        }
        return kontrol;
    }

}
