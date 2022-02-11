package com.example.getcleaner.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.getcleaner.R;
import com.example.getcleaner.objects.Constants;
import com.example.getcleaner.objects.Helper;
import com.example.getcleaner.objects.RateAndComment;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class activity_comments_rates extends AppCompatActivity {
    private RecyclerView main_lst_comments;
    private AdapterComments adapterComments;
    private Activity activity;

    private ImageButton back,logout,myAccount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_comments);

        initElements();
        activity=this;
        String email=getIntent().getExtras().getBundle(Constants.COMMENTS).getString(Constants.USER_EMAIL);

        Helper.getMe().getAllComments(new Helper.callbackCommentsAndRates() {
            @Override
            public void dataReady(ArrayList<RateAndComment> arrayList) {


                    //Adapter(create new adpater fol cleaner )
                    adapterComments = new AdapterComments(activity, arrayList);

                    //control the main_lst_cleaners

                    // Grid
                    main_lst_comments.setLayoutManager(new GridLayoutManager(activity, 1));


                    main_lst_comments.setHasFixedSize(true);
                    main_lst_comments.setItemAnimator(new DefaultItemAnimator());
                    main_lst_comments.setAdapter(adapterComments);

            }
        }, email);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        myAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent manageAccount=new Intent(v.getContext(),activity_manage_my_account.class);
                Bundle bundle=new Bundle();
                bundle.putString(Constants.TYPE,"SEARCHERS");
                bundle.putString(Constants.USER_EMAIL,Helper.getMe().getmAuth().getCurrentUser().getEmail());
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
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    public void initElements(){
        main_lst_comments=findViewById(R.id.main_LST_comments);
        back=findViewById(R.id.back_BTN_comments_Results);
        logout=findViewById(R.id.logout_BTN_comments_Results);
        myAccount=findViewById(R.id.manage_my_account_IB_comments_Results);
    }
}
