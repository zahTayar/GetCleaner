package com.example.getcleaner.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.getcleaner.R;
import com.example.getcleaner.objects.Constants;
import com.example.getcleaner.objects.Helper;
import com.example.getcleaner.objects.userCleaner;
import com.example.getcleaner.Compartors.sortCleanerByHourCost;
import com.example.getcleaner.Compartors.sortCleanerByName;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;

public class activity_cleaners_list extends AppCompatActivity {
    private ImageButton back,logout,myAccount;
    private RecyclerView main_lst_cleaners;
    public String city="";
    private Activity activity;
    private Spinner sortOptions;
    private ArrayList<userCleaner> chosenCleaners;
    private AdapterCleaner adapter_cleaner;
    //for popup
    private TextInputLayout comment_text;
    private ImageButton whatsapp;
    private MaterialButton submit;
    private RatingBar rate;
    private userCleaner user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=this;
        setContentView(R.layout.activity_cleaners_search_results);
        city = getIntent().getExtras().getBundle(Constants.CLEANERS_LIST).getString(Constants.CITY_WANTED);
        initAllElements();
        Helper.getMe().getCleanersSize(new Helper.callbackAll() {
                                           @Override
                                           public void dataReady(ArrayList<userCleaner> array_cleaners) {
                                               //for over the objects find the right ones and divide them for cards
                                               for (int i = 0; i < array_cleaners.size(); i++) {
                                                   //check city match(func)
                                                   chosenCleaners = checkCityMatch(array_cleaners,city);

                                                   //Adapter(create new adpater fol cleaner )
                                                   adapter_cleaner = new AdapterCleaner(activity, chosenCleaners);

                                                   //control the main_lst_cleaners

                                                   // Grid
                                                   main_lst_cleaners.setLayoutManager(new GridLayoutManager(activity, 1));



                                                   main_lst_cleaners.setHasFixedSize(true);
                                                   main_lst_cleaners.setItemAnimator(new DefaultItemAnimator());
                                                   main_lst_cleaners.setAdapter(adapter_cleaner);
                                                   adapter_cleaner.setCleanerItemClickListner(new AdapterCleaner.CleanerItemClickListner() {
                                                       @Override
                                                       public void cleanerItemClicked(userCleaner cleaner, int position) {
                                                           user=cleaner;

                                                           //open pop up and there initial all the elements
                                                           onButtonShowPopupWindowClick(main_lst_cleaners,R.layout.pop_up_comment_and_rate);
                                                           whatsapp.setOnClickListener(new View.OnClickListener() {
                                                               @Override
                                                               public void onClick(View v) {
                                                                   openWhatsappContact(cleaner.getPhoneNumberStr());
                                                               }
                                                           });
                                                           submit.setOnClickListener(new View.OnClickListener() {
                                                               @Override
                                                               public void onClick(View v) {
                                                                   Helper.getMe().addCommentAndRate(Helper.getMe().getmAuth().getCurrentUser().getEmail(),cleaner,getComment(),rate.getRating());
                                                                   updateView(main_lst_cleaners,getIntent().getExtras().getBundle(Constants.CLEANERS_LIST).getString(Constants.USER_EMAIL),city);
//                                                                   finish();
                                                               }
                                                           });
                                                       }

                                                       @Override
                                                       public void goToCommentsScreen(userCleaner cleaner) {
                                                           Intent commentsList=new Intent(main_lst_cleaners.getContext(),activity_comments_rates.class);
                                                           Bundle bundle=new Bundle();
                                                           bundle.putString(Constants.USER_EMAIL,cleaner.getEmailStr());
                                                           commentsList.putExtra(Constants.COMMENTS,bundle);
                                                           startActivity(commentsList);
                                                       }

                                                       @Override
                                                       public void favouriteClicked(userCleaner cleaner, int position) {
                                                           cleaner.setFavorite(!cleaner.isFavorite());
                                                           main_lst_cleaners.getAdapter().notifyItemChanged(position);
                                                       }
                                                   });

                                               }
                                           }
                                       });

        sortOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //find the array list that holds the cleaners
                if(position==1){
                    Collections.sort(chosenCleaners,new sortCleanerByName());
                    adapter_cleaner.notifyDataSetChanged();

                }else if (position==2)
                {
                    Collections.sort(chosenCleaners, new sortCleanerByHourCost());
                    adapter_cleaner.notifyDataSetChanged();

                }
                //sort him by the wat the user asked
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back_to_find_cleaner=new Intent(v.getContext(),activity_menu_find_cleaner.class);
                Bundle bundle=new Bundle();
                bundle.putString(Constants.USER_EMAIL,getIntent().getExtras().getBundle(Constants.CLEANERS_LIST).getString(Constants.USER_EMAIL));
                back_to_find_cleaner.putExtra(Constants.FIND_CLEANER,bundle);
                startActivity(back_to_find_cleaner);
                finish();
            }
        });
        myAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent manageAccount=new Intent(v.getContext(),activity_manage_my_account.class);
                Bundle bundle=new Bundle();
                bundle.putString(Constants.TYPE,"SEARCHERS");
                bundle.putString(Constants.USER_EMAIL,getIntent().getExtras().getBundle(Constants.CLEANERS_LIST).
                        getString(Constants.USER_EMAIL));
                manageAccount.putExtra(Constants.BUNDLE_MANAGE_ACCOUNT,bundle);
                startActivity(manageAccount);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save all changes if done
                //logout from the user
                FirebaseAuth.getInstance().signOut();
                //move to screen find cleaner
                Intent logout_to_start_menu_from_list=new Intent(v.getContext(),activity_start_menu.class);
                startActivity(logout_to_start_menu_from_list);
                //finish
                finish();
            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    public ArrayList<userCleaner> checkCityMatch(ArrayList<userCleaner> array_cleaners,String city)
    {
        ArrayList<userCleaner> chosen=new ArrayList();
        for (userCleaner user:array_cleaners)
        {
            if(user.getAddressStr().equals(city))
            {
                chosen.add(user);
            }
        }
        return chosen;
    }
    public void initAllElements()
    {
        main_lst_cleaners=findViewById(R.id.list_of_Cleaners);
        back=findViewById(R.id.back_BTN_search_results);
        logout=findViewById(R.id.logout_BTN_search_results);
        myAccount=findViewById(R.id.manage_my_account_IB_cleaners_list);
        //spinner
        sortOptions=findViewById(R.id.sort_spn_options);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Constants.possibleSort);
        //set the spinners adapter to the previously created one.
        sortOptions.setAdapter(adapter);

    }
    public void onButtonShowPopupWindowClick(View view, int layout) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(layout, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        initial_pop_up(popupView);
        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.TOP, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }
    public void updateView(View view,String email,String city)
    {
        finish();
        Intent myIntent=new Intent(view.getContext(),activity_cleaners_list.class);
        Bundle bundle=new Bundle();
        bundle.putString(Constants.CITY_WANTED,city);
        bundle.putString(Constants.USER_EMAIL,email);
        myIntent.putExtra(Constants.CLEANERS_LIST,bundle);
        startActivity(myIntent);
    }
    private void initial_pop_up(View view)
    {
            rate=view.findViewById(R.id.ratingBar);
            comment_text=view.findViewById(R.id.fram_pop_up_comment);
            submit=view.findViewById(R.id.btnSubmit);
            whatsapp=view.findViewById(R.id.whatsapp);
    }
    private String getComment() {
        return comment_text.getEditText().getText().toString();
    }
    void openWhatsappContact(String number) {
        Uri uri = Uri.parse("smsto:" + number);
        Intent i = new Intent(Intent.ACTION_SENDTO, uri);
        i.setPackage("com.whatsapp");
        startActivity(Intent.createChooser(i, ""));
    }


}
