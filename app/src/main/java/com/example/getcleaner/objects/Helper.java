package com.example.getcleaner.objects;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.getcleaner.R;
import com.example.getcleaner.activities.activity_sign_cleaner;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executor;

public class Helper {
    public FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private static Helper me;

    public static Helper getMe() {
        return me;
    }
    public Helper()
    {
         database = FirebaseDatabase.getInstance();
         mAuth = FirebaseAuth.getInstance();
    }
    public static Helper initHelper() {
        if (me == null) {
            me = new Helper();
        }
        return me;
    }


    public boolean checkAllInputs(Context context,String emailStr,String passwordStr,String verifyPasswordStr,
                                  String addressStr,String firstNameStr,String lastNameStr,String phoneNumberStr)
    {

        int duration = Toast.LENGTH_SHORT;
        String text="";
        Toast toast = Toast.makeText(context, text, duration);
        if(!emailStr.contains("@"))
        {
            text+="email ";
        }
        //check if email exist
//        database.getReference("USERS").child(emailStr).

        if(!(passwordStr.length()>4))
        {
            text+="password ";
        }

        if(!(verifyPasswordStr.length()>4))
        {
            text+="VerifyPassword ";
        }
        if(!passwordStr.equals(verifyPasswordStr))
        {
            text+="passwordsNotMatch";
        }

        if(!(addressStr.length()>0))
        {
            text+="address ";
        }
        if(!(firstNameStr.length()>0))
        {
            text+="firstName ";
        }

        if(!(lastNameStr.length()>0))
        {
            text+="lastName ";
        }

        if(!(phoneNumberStr.length()==10))
        {
            text+="phoneNumber ";
        }
        if(text.length()>1){
            text+="not entered well , Please verify it again.";
            return false;
        }else{

            text+="sign in was successfull";
        }
        toast.setText(text);
        toast.show();
        return true;
    }

    public void addCleaner(userCleaner newUserClean){
        DatabaseReference myCleaners=database.getReference("CLEANERS");
        myCleaners.child((newUserClean.getEmailStr().split("\\.")[0]).toUpperCase()).setValue(newUserClean);
        mAuth.createUserWithEmailAndPassword(newUserClean.getEmailStr(),newUserClean.getPasswordStr()).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                        }else{
                            //Toast message
                        }
                    }
                });

    }
    public boolean check_input_cleaners(String minimumPerHour)
    {
        if(!(Integer.parseInt(minimumPerHour)>0)){
            return false;
        }
        return true;
    }

    public interface callbackCleanr{
        void cleanerDataReady(userCleaner user);
    }

    public void getCleaner(String userEmail,callbackCleanr cleanrCallback)
    {
        DatabaseReference myCleaners=database.getReference("CLEANERS");
        myCleaners.child((userEmail.split("\\.")[0]).toUpperCase()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userCleaner tmp=snapshot.getValue(userCleaner.class);
                if(cleanrCallback!=null){
                    cleanrCallback.cleanerDataReady(tmp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addSearcher(userSearcher newUserSearch, Activity activity){
        DatabaseReference mySearchers=database.getReference("SEARCHERS");
        mySearchers.child((newUserSearch.getEmailStr().split("\\.")[0]).toUpperCase()).setValue(newUserSearch);

        mAuth.createUserWithEmailAndPassword(newUserSearch.getEmailStr(),newUserSearch.getPasswordStr()).addOnCompleteListener(
                activity,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                        }else{
                            //Toast message
                        }
                    }
                });
        int x=0;
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public interface CallBackSearcher{
        void searcherDataReady(userSearcher user);
    }
    public void getSearcher(String email,CallBackSearcher searcherCallback){
        DatabaseReference mySearchers=database.getReference("SEARCHERS");
        mySearchers.child((email.split("\\.")[0]).toUpperCase()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userSearcher user=snapshot.getValue(userSearcher.class);
                searcherCallback.searcherDataReady(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public interface callbackAll{
        void dataReady(ArrayList<userCleaner> arrayList);
    }
    public void getCleanersSize(callbackAll callback){
        DatabaseReference myCleaners= database.getReference("CLEANERS");
        myCleaners.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<userCleaner> arrayList=new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    arrayList.add(child.getValue(userCleaner.class));
                }
                callback.dataReady(arrayList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public interface callbackCommentsAndRates{
        void dataReady(ArrayList<RateAndComment> arrayList);
    }
    public void getAllComments(callbackCommentsAndRates callbackCommentsAndRates,String email){
        DatabaseReference myComments = database.getReference("RATES").child(email.split("\\.")[0].toUpperCase());
        myComments.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<RateAndComment> allComments=new ArrayList<>();
                for(DataSnapshot child: snapshot.getChildren()){
                    allComments.add(child.getValue(RateAndComment.class));
                }
                callbackCommentsAndRates.dataReady(allComments);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void addCommentAndRate(String email ,userCleaner user, String comment,float rate)
    {
        //check if user not exist  if not create new if exist add it to the
        DatabaseReference myComments=database.getReference("RATES");
        myComments.child(user.getEmailStr().split("\\.")[0].toUpperCase()).push().setValue(new RateAndComment().setComment(comment).setRate(rate).setByHow(email.split("\\.")[0]));
    }



}
