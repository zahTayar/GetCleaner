package com.example.getcleaner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.getcleaner.R;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.getcleaner.objects.Constants;
import com.example.getcleaner.objects.Helper;
import com.example.getcleaner.objects.userSearcher;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class activity_menu_find_cleaner extends AppCompatActivity {
    private MaterialButton findCleaner;
    private ImageButton logout,myAccount;
    private TextView hi;
    private Spinner dropdown_citys;
    private TextInputLayout city;

    public int chosenPosition=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_find_cleaner);
        initAllElements();

        dropdown_citys.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                chosenPosition=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        findCleaner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //search in DB all results in specific town

                //init all cleaners cards

                //move to screen cleaners list
                Intent cleaners_list = new Intent(v.getContext(),activity_cleaners_list.class);
                Bundle bundle=new Bundle();
                bundle.putString(Constants.USER_EMAIL,getIntent().getExtras().getBundle(Constants.FIND_CLEANER).
                        getString(Constants.USER_EMAIL));
                bundle.putString(Constants.CITY_WANTED,Constants.items[chosenPosition]);

                cleaners_list.putExtra(Constants.CLEANERS_LIST,bundle);
                startActivity(cleaners_list);
                //finish
                finish();
            }
        });
        myAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent manageAccount=new Intent(v.getContext(),activity_manage_my_account.class);
                Bundle bundle=new Bundle();
                bundle.putString(Constants.TYPE,"SEARCHERS");
                bundle.putString(Constants.USER_EMAIL,getIntent().getExtras().getBundle(Constants.FIND_CLEANER).
                        getString(Constants.USER_EMAIL));
                manageAccount.putExtra(Constants.BUNDLE_MANAGE_ACCOUNT,bundle);
                startActivity(manageAccount);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save all data if needed
                //log out from user
                FirebaseAuth.getInstance().signOut();
                //move to screen start menu
                Intent log_to_find_cleaner=new Intent(v.getContext(),activity_start_menu.class);
                startActivity(log_to_find_cleaner);
                //finish
                finish();
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    public void initAllElements()
    {
        findCleaner=findViewById(R.id.find_cleaner_BTN);
        logout=findViewById(R.id.logout_BTN_find_cleaner);
        myAccount=findViewById(R.id.frame1_IB_manage_my_account);
        dropdown_citys=findViewById(R.id.spinner_towns);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Constants.items);
        //set the spinners adapter to the previously created one.
        dropdown_citys.setAdapter(adapter);
        hi=findViewById(R.id.hi_TV_with_name);
        Helper.getMe().getSearcher(getIntent().getExtras().getBundle(Constants.FIND_CLEANER).getString(Constants.USER_EMAIL), new Helper.CallBackSearcher() {
            @Override
            public void searcherDataReady(userSearcher user) {
                hi.setText(hi.getText()+" "+user.getFirstNameStr()+" "+user.getLastNameStr());
            }
        });

    }
}
