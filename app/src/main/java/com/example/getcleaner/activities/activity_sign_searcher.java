package com.example.getcleaner.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.getcleaner.R;
import com.example.getcleaner.objects.Constants;
import com.example.getcleaner.objects.Helper;
import com.example.getcleaner.objects.userSearcher;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.Duration;
import java.util.HashMap;

public class activity_sign_searcher extends AppCompatActivity {
    private MaterialButton back;
    private MaterialButton sign;
    private TextInputLayout email,password,verifyPassword,firstName,lastName,phoneNumber;
    private Activity activity;
    private Spinner spinner;
    private int positionChosen;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_searcher);
        initElements();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                positionChosen=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        activity=this;
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //send to function to get all data and verified its OK if not message as excepted

                if(Helper.getMe().checkAllInputs(v.getContext(),getEmail(),getPassword(),
                        getPasswordVerify(),getAddress(),getFirstName(),getLastName(),getPhoneNumber()))
                {
                    userSearcher newUserSearch = new userSearcher(getEmail(),getPassword(),
                            getAddress(),getFirstName(),getLastName(),getPhoneNumber());
                    //save to db
                    Helper.getMe().addSearcher(newUserSearch,activity);
                        //make a toast message
                    Toast toast=Toast.makeText(v.getContext(),"User creates succussfully",Toast.LENGTH_SHORT);


                    //move to screen find cleaner
                    Intent find_cleaner_intent = new Intent(v.getContext(),activity_menu_find_cleaner.class);
                    Bundle bundle=new Bundle();
                    bundle.putString(Constants.USER_EMAIL,getEmail());
                    find_cleaner_intent.putExtra(Constants.FIND_CLEANER,bundle);
                    startActivity(find_cleaner_intent);
                    //finish
                    finish();
                }

            }
        });
    }

    public void initElements()
    {
        back=findViewById(R.id.searcher_btn_back);
        sign = findViewById(R.id.sign_BTN_searcher);

        //helper

        //text input fields.
        email = findViewById(R.id.frame1_EDT_username);
        password = findViewById(R.id.frame1_EDT_password);
        verifyPassword = findViewById(R.id.frame1_EDT_password_verify);
        firstName = findViewById(R.id.frame1_EDT_first_name);
        lastName = findViewById(R.id.frame1_EDT_last_name);
        phoneNumber = findViewById(R.id.frame1_EDT_phone_number);

        //spinner
        spinner=findViewById(R.id.frame1_EDT_address);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Constants.items);
//set the spinners adapter to the previously created one.
        spinner.setAdapter(adapter);
    }

    private String getEmail() {
        return email.getEditText().getText().toString();
    }
    private String getPassword() {
        return password.getEditText().getText().toString();
    }
    private String getPasswordVerify() {
        return verifyPassword.getEditText().getText().toString();
    }
    private String getAddress() {
        return Constants.items[positionChosen];
    }

    private String getFirstName() {
        return firstName.getEditText().getText().toString();
    }
    private String getLastName() {
        return lastName.getEditText().getText().toString();
    }
    private String getPhoneNumber() {
        return phoneNumber.getEditText().getText().toString();
    }



}
