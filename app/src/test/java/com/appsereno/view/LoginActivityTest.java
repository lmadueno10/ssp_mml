package com.appsereno.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import com.appsereno.R;
import com.appsereno.model.base.BaseApplication;
import com.appsereno.model.entities.Login;
import com.appsereno.model.webservice.LoginService;
import com.appsereno.viewmodel.adapter.LoginViewModel;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


import io.reactivex.Observable;
import io.reactivex.Observer;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LoginActivityTest {

    @Mock
    private LoginService loginService;
    @InjectMocks
    private LoginViewModel loginViewModel=new LoginViewModel();

    @Before
    public void init(){
        Observable<Login> objectReturn= new Observable<Login>() {
            @Override
            protected void subscribeActual(Observer<? super Login> observer) {

            }
        };
        when(loginService.getLoginObservable("user","pass")).thenReturn(objectReturn);

    }
    @Test
    public void login(){
        loginViewModel.getLogin("user","pass");
    }
}