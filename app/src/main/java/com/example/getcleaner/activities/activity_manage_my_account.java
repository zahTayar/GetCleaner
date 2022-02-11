package com.example.getcleaner.activities;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.getcleaner.R;
import com.example.getcleaner.objects.Constants;
import com.example.getcleaner.objects.Helper;
import com.example.getcleaner.objects.userCleaner;
import com.example.getcleaner.objects.userSearcher;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class activity_manage_my_account extends AppCompatActivity
{
    private TextView perHourTitle,specialTitle,mobilityTitle;
    private TextView firstNameTv,lastNameTv,phoneNumberTv,addressTv,perHourTv,specialTv,mobilityTv;
    private ImageButton firstNameBtn,lastNameBtn,phoneNumberBtn,addressBtn,perHourBtn,specialBtn,mobilityBtn;
    private ImageButton back,logout;
    private  MaterialButton myComments;
    //pop up
    private MaterialButton change;
    private TextInputLayout popup;
    private String theType="";
    private Spinner spinner;
    private CheckBox changeSpecialOrMobility;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_my_account);
        String email = getIntent().getExtras().getBundle(Constants.BUNDLE_MANAGE_ACCOUNT).getString(Constants.USER_EMAIL);
        String type = getIntent().getExtras().getBundle(Constants.BUNDLE_MANAGE_ACCOUNT).getString(Constants.TYPE);
        initAllElements();
        if(type.equals("SEARCHERS"))
        {
            theType = "SEARCHERS";
            setInvisableForSearcher();
            Helper.getMe().getSearcher(email, new Helper.CallBackSearcher() {
                @Override
                public void searcherDataReady(userSearcher user) {
                    firstNameTv.setText(user.getFirstNameStr());
                    lastNameTv.setText(user.getLastNameStr());
                    phoneNumberTv.setText(user.getPhoneNumberStr());
                    addressTv.setText(user.getAddressStr());
                }
            });


        }else
            {
            theType="CLEANERS";
            setVisableForCleaner();
            Helper.getMe().getCleaner(email, new Helper.callbackCleanr() {
                @Override
                public void cleanerDataReady(userCleaner user_cleaner) {
                    firstNameTv.setText(user_cleaner.getFirstNameStr());
                    lastNameTv.setText(user_cleaner.getLastNameStr());
                    phoneNumberTv.setText(user_cleaner.getPhoneNumberStr());
                    addressTv.setText(user_cleaner.getAddressStr());
                    perHourTv.setText(user_cleaner.getPerHour());
                    if(user_cleaner.isMobility()){
                        mobilityTv.setText("Yes, I'm Mobile");
                    }else{
                        mobilityTv.setText("No, I'm Not Mobile");

                    }
                    if(user_cleaner.isReadyForSpecial()){
                        specialTv.setText("Yes, I'm doing special missions");
                    }else{
                        specialTv.setText("No, I'm Not doing special missions");

                    }

                }
            });

            myComments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent commentsScreen=new Intent(v.getContext(),activity_comments_rates.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.USER_EMAIL,Helper.getMe().getmAuth().getCurrentUser().getEmail());
                    commentsScreen.putExtra(Constants.COMMENTS,bundle);
                    startActivity(commentsScreen);

                }
            });
        }
        if(perHourBtn.getVisibility()==View.VISIBLE){
            perHourBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    onButtonChangeData(v,theType,email,"perHour",R.layout.pop_up_for_change);
                }
            });
        }
        if(specialBtn.getVisibility()==View.VISIBLE){
            specialBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtonChangeData(v,theType,email,"readyForSpecial",R.layout.pop_up_for_checkbox);
                }
            });
        }
        if(mobilityBtn.getVisibility()==View.VISIBLE){
            mobilityBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtonChangeData(v,theType,email,"mobility",R.layout.pop_up_for_checkbox);
                    //how to change
                }
            });
        }
        firstNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonChangeData(v,theType,email,"firstNameStr",R.layout.pop_up_for_change);
            }
        });
        lastNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonChangeData(v,theType,email,"lastNameStr",R.layout.pop_up_for_change);
            }
        });
        phoneNumberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonChangeData(v,theType,email,"phoneNumberStr",R.layout.pop_up_for_change);
            }
        });
        addressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonChangeData(v,theType,email,"addressStr",R.layout.pop_up_for_address);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
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

    @Override
    protected void onStart() {
        super.onStart();
    }
    public void updateView(View view,String email,String type)
    {
        finish();
        Intent myIntent=new Intent(view.getContext(),activity_manage_my_account.class);
        Bundle bundle=new Bundle();
        bundle.putString(Constants.TYPE,type);
        bundle.putString(Constants.USER_EMAIL,email);
        myIntent.putExtra(Constants.BUNDLE_MANAGE_ACCOUNT,bundle);
        startActivity(myIntent);
    }
    public void onButtonChangeData(View v,String type,String email,String key,int layout){

        //open text view to write the change
        onButtonShowPopupWindowClick(v,layout);
        //get the user from db
        if (layout == R.layout.pop_up_for_change) {
            change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Helper.getMe().database.getReference(type).child(email.split("\\.")[0].toUpperCase()).child(key).setValue(getPopUp());
                    updateView(v,email,type);
                }
            });
        }else if (layout==R.layout.pop_up_for_address){
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Helper.getMe().database.getReference(type).child(email.split("\\.")[0].toUpperCase()).child(key).setValue(Constants.items[position]);
                    if(position!=0){
                        updateView(view,email,type);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }else{
            Helper.getMe().database.getReference(type).child(email.split("\\.")[0].toUpperCase()).child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getValue(boolean.class)){
                        changeSpecialOrMobility.setChecked(true);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            changeSpecialOrMobility.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Helper.getMe().database.getReference(type).child(email.split("\\.")[0].toUpperCase()).child(key).setValue(changeSpecialOrMobility.isChecked());
                    updateView(v,email,type);
                }

            });
        }


    }
    public void onButtonShowPopupWindowClick(View view, int layout) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(layout, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        if(layout==R.layout.pop_up_for_change){
            change=popupView.findViewById(R.id.fram_btn_pop_change);
            popup=popupView.findViewById(R.id.fram_pop_up_change);
        }else if (layout==R.layout.pop_up_for_address){
            spinner=popupView.findViewById(R.id.spinner_for_city_change);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Constants.items);
//set the spinners adapter to the previously created one.
            spinner.setAdapter(adapter);
        }else{
            changeSpecialOrMobility=popupView.findViewById(R.id.checkbox_change);
        }

        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }
    public void setInvisableForSearcher()
    {
        myComments.setVisibility(View.INVISIBLE);

        mobilityTitle.setVisibility(View.INVISIBLE);
        perHourTitle.setVisibility(View.INVISIBLE);
        specialTitle.setVisibility(View.INVISIBLE);
        specialBtn.setVisibility(View.INVISIBLE);
        specialTv.setVisibility(View.INVISIBLE);
        perHourBtn.setVisibility(View.INVISIBLE);
        mobilityTv.setVisibility(View.INVISIBLE);
        perHourTv.setVisibility(View.INVISIBLE);
        mobilityBtn.setVisibility(View.INVISIBLE);
    }
    public void setVisableForCleaner()
    {
        myComments.setVisibility(View.VISIBLE);

        mobilityTitle.setVisibility(View.VISIBLE);
        perHourTitle.setVisibility(View.VISIBLE);
        specialTitle.setVisibility(View.VISIBLE);
        specialBtn.setVisibility(View.VISIBLE);
        specialTv.setVisibility(View.VISIBLE);
        perHourBtn.setVisibility(View.VISIBLE);
        mobilityTv.setVisibility(View.VISIBLE);
        perHourTv.setVisibility(View.VISIBLE);
        mobilityBtn.setVisibility(View.VISIBLE);
    }
    public void initAllElements()
    {
        //btn
        back=findViewById(R.id.back_BTN_manage_account);
        logout=findViewById(R.id.logout_BTN_manage_account);

        //text view for hide
        mobilityTitle=findViewById(R.id.Mobility_TV_title);
        perHourTitle=findViewById(R.id.Per_hour_TV_title);
        specialTitle=findViewById(R.id.Special_missions_TV_title);

        //text view initialize
        firstNameTv = findViewById(R.id.First_name_TV_show);
        lastNameTv = findViewById(R.id.Last_name_TV_show);
        phoneNumberTv = findViewById(R.id.Phone_number_TV_show);
        addressTv = findViewById(R.id.Address_TV_show);
        perHourTv = findViewById(R.id.Per_hour_TV_show);
        specialTv = findViewById(R.id.Special_missions_TV_show);
        mobilityTv = findViewById(R.id.Mobility_TV_show);

        //Image buttons initialize
        firstNameBtn=findViewById(R.id.First_name_BTN_edit);
        lastNameBtn=findViewById(R.id.Last_name_BTN_edit);
        phoneNumberBtn=findViewById(R.id.Phone_number_BTN_edit);
        addressBtn=findViewById(R.id.Address_BTN_edit);
        perHourBtn=findViewById(R.id.Per_hour_BTN_edit);
        specialBtn=findViewById(R.id.Special_missions_BTN_edit);
        mobilityBtn=findViewById(R.id.Mobility_BTN_edit);

        myComments=findViewById(R.id.my_comments_cleaner);

        // popup
    }
    private String getPopUp() {
        return popup.getEditText().getText().toString();
    }


}
