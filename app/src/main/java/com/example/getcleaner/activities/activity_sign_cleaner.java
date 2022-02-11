package com.example.getcleaner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.getcleaner.R;
import com.example.getcleaner.objects.Constants;
import com.example.getcleaner.objects.Helper;
import com.example.getcleaner.objects.userCleaner;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class activity_sign_cleaner extends AppCompatActivity {
    private MaterialButton back,sign;
    private TextInputLayout email,password,verifyPassword,address,firstName,lastName,phoneNumber,minimum;
    private Spinner work_location;
    private CheckBox mobilityCB,specialMissionsCB;
    private int positionChosen;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_cleaner);
        initElements();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        work_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                positionChosen=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send to function to get all data and verified its OK if not message as excepted
                if(!Helper.getMe().checkAllInputs(v.getContext(),getEmail(),getPassword(),getPasswordVerify(),getAddress(),getFirstName(),getLastName()
                ,getPhoneNumber())){
                    //toast message
                    Toast.makeText(activity_sign_cleaner.this,"Hi you have entered worng input\nPlease verify and try again.",Toast.LENGTH_SHORT);

                }
                if(!Helper.getMe().check_input_cleaners(getMinimum()))
                {
                    //toast message
                    Toast.makeText(activity_sign_cleaner.this,"Incorrect value for minimum per hour\n Please try again.",Toast.LENGTH_SHORT);
                }
                //deal with answers

                userCleaner newUserCleaner=new userCleaner(getEmail(),getPassword(),getAddress(),getFirstName(),getLastName(),getPhoneNumber()
                ,getMinimum(),getSpecialMission(),getMobility());
                //save to db
                Helper.getMe().addCleaner(newUserCleaner);
                //make a toast message
                Toast.makeText(activity_sign_cleaner.this,"Cleaner was added successfully.",Toast.LENGTH_SHORT);
                //move to screen manage my Account
                Intent manage_my_account = new Intent(v.getContext(),activity_manage_my_account.class);
                Bundle bundle=new Bundle();
                bundle.putString(Constants.USER_EMAIL,getEmail());
                bundle.putString(Constants.TYPE,"cleaner");
                manage_my_account.putExtra(Constants.BUNDLE_MANAGE_ACCOUNT,bundle);
                startActivity(manage_my_account);
                //finish
                finish();
            }
        });
    }
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
    }

    public void initElements(){
        //check box
        mobilityCB=findViewById(R.id.checkbox_mobility);
        specialMissionsCB=findViewById(R.id.checkbox_special);
        //image buttons
        sign = findViewById(R.id.sign_BTN_cleaner);
        back=findViewById(R.id.cleaner_btn_back);
        //text input fields.
        email = findViewById(R.id.frame2_EDT_username);
        password = findViewById(R.id.frame2_EDT_password);
        verifyPassword = findViewById(R.id.frame2_EDT_password_verify);

        firstName = findViewById(R.id.frame2_EDT_first_name);
        lastName = findViewById(R.id.frame2_EDT_last_name);
        phoneNumber = findViewById(R.id.frame2_EDT_phone_number);
        minimum = findViewById(R.id.frame2_EDT_per_hour);

        //sppiner
        work_location= findViewById(R.id.frame2_EDT_address);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Constants.items);
//set the spinners adapter to the previously created one.
        work_location.setAdapter(adapter);

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
    private String getMinimum() {
        return minimum.getEditText().getText().toString();
    }
    private boolean getSpecialMission() {
        return specialMissionsCB.isChecked();
    }
    private boolean getMobility() {
        return mobilityCB.isChecked();
    }

}
