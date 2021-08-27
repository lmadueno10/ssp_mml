package com.appsereno.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.appsereno.R;
import com.appsereno.model.base.BaseApplication;
import com.appsereno.viewmodel.adapter.LoginViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;
/**
 * LoginActivity is the class that is bound to the activity_login.xml view
 * defines the behavior of the view when the user interacts
 */
public class LoginActivity extends AppCompatActivity {
    private TextInputEditText editTextPassword;
    private TextInputEditText editTextUsuario;
    private CheckBox cbxActive;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferences=getSharedPreferences("credenciales",Context.MODE_PRIVATE);
        editTextPassword= findViewById(R.id.editTextTextPassword);
        editTextUsuario= findViewById(R.id.editTextTextUsuario);
        Button btnSignin = findViewById(R.id.btn_signin);
        cbxActive= findViewById(R.id.cbxActive);
        btnSignin.setOnClickListener(this::logIn);
        SharedPreferences preferences=getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        String refreshToken=preferences.getString("refresh_token","0");
        boolean keepAlive=preferences.getBoolean("keep_alive",false);
        if(!refreshToken.equals("0")&&keepAlive){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }


    public void logIn(View v){
        try {
            if (Objects.requireNonNull(editTextUsuario.getText()).toString()
                    .isEmpty()) {
                editTextUsuario.setError("Ingrese un usuario válido");
            } else if (Objects.requireNonNull(editTextPassword.getText()).toString().isEmpty()) {
                editTextPassword.setError("Ingrese una contraseña válida");
            } else {
                LoginViewModel loginViewModel = new LoginViewModel(((BaseApplication) getApplication()));

                loginViewModel.signIn(editTextUsuario.getText().toString(), editTextPassword.getText().toString(), v, preferences, this, cbxActive.isChecked());
            }
        }catch (NullPointerException e){
            Log.d("ERROR",e.getMessage());
        }
    }
    public void initProgress(){
        LinearLayout ll=findViewById(R.id.opacity_login);
        ll.setVisibility(View.VISIBLE);

    }

    public void endProgress(){
        LinearLayout ll=findViewById(R.id.opacity_login);
        ll.setVisibility(View.GONE);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}