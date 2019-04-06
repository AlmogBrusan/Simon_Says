package com.example.simonsaysproject;

import android.support.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HighScoreManager
{
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user-score");
    private List<UserScore> highScores = new ArrayList<>();

    public HighScoreManager(){}

    //Push user scores into database
    public void pushUserScore(String name, int score , String level)
    {
        UserScore userScore= new UserScore(name, score , level );
        String key = reference.push().getKey();
        if(key!=null)
            reference.child(key).setValue(userScore);
    }

    //Request highest scores from database
    public void getHighScore(final highScoreListener listener)
    {
        Query query = reference.orderByChild("score").limitToLast(15);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                highScores.clear();
                for(DataSnapshot scoreSnapshot:dataSnapshot.getChildren())
                {
                    UserScore userScore = scoreSnapshot.getValue(UserScore.class);
                    highScores.add(userScore);
                }
                //Reverse the order of elements in the list
                Collections.reverse(highScores);
                listener.onChange(highScores);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onError();

            }
        });

    }

    public interface highScoreListener
    {
        void onChange(List<UserScore> highScores);
        void onError();
    }


}
