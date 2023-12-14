package vn.edu.hcmuaf.fit.gameteambulding;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.StyledPlayerView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class EvaluateView extends AppCompatActivity {
TextView name;
String competitionPath;
String competitionID;
    FragmentManager fragmentManager;
Button next, previous;
    Candidate1Fragment f1= new Candidate1Fragment();
    Candidate2Fragment f2= new Candidate2Fragment();
String candi1_ID,candi2_ID;
    String url="https://firebasestorage.googleapis.com/v0/b/gameteambulding.appspot.com/o/videos%2F05a88dbf-e5a9-4b8f-9fc6-3ecdb9aa1366?alt=media&token=40913e06-0769-4a85-a9cd-61dee4b19999";


ExoPlayer player;

    FirebaseFirestore  db= FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate_view);


        next=findViewById(R.id.next_candidate);
        previous=findViewById(R.id.previous_candidate);
        //load title start
        name=findViewById(R.id.id);

        if(getIntent().getExtras()!=null){
            if (getIntent().getExtras().getString("competitionPath")!=null){
            competitionPath =getIntent().getExtras().getString("competitionPath");}
            else  competitionPath ="F1KOzoe56XhzzevycpRC";
        }else{
            competitionPath ="F1KOzoe56XhzzevycpRC";}
        DocumentReference docRef = db.collection("COMPETITION2").document(competitionPath);
        docRef.addSnapshotListener( new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                //sorry, id field is name field instead
                name.setText(value.getString("title"));
               // compID.setText(docRef.getPath().substring(docRef.getPath().indexOf("/")+1,docRef.getPath().length()));
                competitionID=docRef.getPath().substring(docRef.getPath().indexOf("/")+1,docRef.getPath().length());

            }
        });

        //load title end
        previous.setEnabled(false);
       changeFragment(f1);
       //add next button listener
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               changeFragment(f2);
                previous.setEnabled(true);
                next.setEnabled(false);
            }
        });
        //add prev listener
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             changeFragment(f1);
                previous.setEnabled(false);
                next.setEnabled(true);
            }
        });
    }
public void changeFragment(Fragment fragment){
        if (fragment!=null){
             fragmentManager= getSupportFragmentManager();
            FragmentTransaction ft=fragmentManager.beginTransaction();
            ft.replace(R.id.fragment_area,fragment);
            ft.addToBackStack(fragment.getClass().getSimpleName());
            ft.commit();
        }
}
public void addFragment(Fragment fragment){
        fragmentManager=getSupportFragmentManager();
        FragmentTransaction ft=fragmentManager.beginTransaction();
        ft.add(R.id.fragment_area,fragment);
        ft.addToBackStack(fragment.getClass().getSimpleName());
        ft.commit();
}

}