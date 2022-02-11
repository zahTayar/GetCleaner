package com.example.getcleaner.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.example.getcleaner.R;
import com.example.getcleaner.objects.Constants;
import com.example.getcleaner.objects.Helper;
import com.example.getcleaner.objects.userCleaner;
import com.example.getcleaner.objects.userSearcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class activity_start_menu extends AppCompatActivity {
    private MaterialButton sign_as_searcher,connect;
    private MaterialButton sign_as_cleaner;
    private TextInputLayout username,password;
    private String usernameStr,passwordStr;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);
        Helper.initHelper();
        initElements();

        sign_as_searcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if exist user with the entered values
                Intent myIntent = new Intent(v.getContext(), activity_sign_searcher.class);
                startActivity(myIntent);
            }
        });
        sign_as_cleaner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), activity_sign_cleaner.class);
                startActivity(myIntent);
            }
        });
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth=FirebaseAuth.getInstance();
                mAuth.signInWithEmailAndPassword(getUserName(), getPassword())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information

                                    FirebaseUser user = mAuth.getCurrentUser();
                                    figureWhichUser(user,v);
//                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.

                                    Toast.makeText(activity_start_menu.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                //check if user name and password is in db
                //if user is cleaner so to screen 1 if seracher to screen two
            }
        });

    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    public void figureWhichUser(FirebaseUser user, View v){
        Helper.getMe().getCleaner(user.getEmail(), new Helper.callbackCleanr() {
            @Override
            public void cleanerDataReady(userCleaner user) {
                if (user!=null){
                    updateToCleaner(user.getEmailStr(),v);
                }
            }
        });
        Helper.getMe().getSearcher(user.getEmail(), new Helper.CallBackSearcher() {
            @Override
            public void searcherDataReady(userSearcher user) {
                if (user!=null){
                    updateToSearcher(user.getEmailStr(),v);
                }
            }
        });

    }
    public void updateToSearcher(String email, View v){
        Intent find_cleaner_intent = new Intent(v.getContext(),activity_menu_find_cleaner.class);
        Bundle bundle=new Bundle();
        bundle.putString(Constants.USER_EMAIL,email);
        find_cleaner_intent.putExtra(Constants.FIND_CLEANER,bundle);
        startActivity(find_cleaner_intent);
    }
    public void updateToCleaner(String email,View v){

        Intent manage_my_account = new Intent(v.getContext(),activity_manage_my_account.class);
        Bundle bundle=new Bundle();
        bundle.putString(Constants.USER_EMAIL,email);
        bundle.putString(Constants.TYPE,"cleaner");
        manage_my_account.putExtra(Constants.BUNDLE_MANAGE_ACCOUNT,bundle);
        startActivity(manage_my_account);
    }
    public void initElements()
    {
        sign_as_searcher=findViewById(R.id.start_btn_searcher);
        sign_as_cleaner=findViewById(R.id.start_btn_cleaner);
        connect=findViewById(R.id.connect_BTN_start);
        username=findViewById(R.id.frame0_EDT_username);
        password=findViewById(R.id.frame0_EDT_password);
    }
    private String getUserName() {
        return username.getEditText().getText().toString();
    }
    private String getPassword() { return password.getEditText().getText().toString(); }

}