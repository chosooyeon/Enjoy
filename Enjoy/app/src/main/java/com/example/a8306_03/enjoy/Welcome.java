package com.example.a8306_03.enjoy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

public class Welcome extends AppCompatActivity
{

    TextInputLayout ti_1;
    TextInputLayout ti_2;
    AppCompatEditText et_1;
    AppCompatEditText et_2;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_enjoy);

        ti_1 = (TextInputLayout)findViewById(R.id.ti_1);
        ti_2 = (TextInputLayout)findViewById(R.id.ti_2);

        et_1 = (AppCompatEditText)findViewById(R.id.et_1);
        et_2 = (AppCompatEditText)findViewById(R.id.et_2);

        et_1.addTextChangedListener(idTextWatcher);
        et_2.addTextChangedListener(pwTextWatcher);

        ti_1.setCounterEnabled(true);
        ti_2.setCounterEnabled(true);


        ti_1.setErrorEnabled(true);
        ti_2.setErrorEnabled(true);



        ti_1.setCounterMaxLength(100);
        ti_2.setCounterMaxLength(100);


    }

    TextWatcher idTextWatcher = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {

        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {

        }
        @Override
        public void afterTextChanged(Editable s)
        {
            if(s.length()>10) {
                ti_1.setError("error");
            }
            else
            {
                ti_1.setError(null);
            }
        }
    };

    TextWatcher pwTextWatcher = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {

        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {

        }
        @Override
        public void afterTextChanged(Editable s)
        {
            if(s.length()>10)
            {
                ti_2.setError("error2");
            }
            else
            {
                ti_2.setError(null);
            }
        }
    };

    public void mLogin(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
