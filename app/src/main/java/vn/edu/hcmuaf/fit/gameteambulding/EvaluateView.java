package vn.edu.hcmuaf.fit.gameteambulding;


import static androidx.fragment.app.FragmentManager.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.StyledPlayerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

public class EvaluateView extends AppCompatActivity {
TextView name;
ExoPlayer player;
String competitionPath;
boolean isFirst;
String competitionID;
    FragmentManager fragmentManager;
Button next, previous, save;
    Candidate1Fragment f1;

    Candidate2Fragment f2;
    EditText edit;  FirebaseAuth auth= FirebaseAuth.getInstance();
String candi1_ID,candi2_ID,vid1, vid2,entryID1,entryID2, current,rvID;
    String url="https://firebasestorage.googleapis.com/v0/b/gameteambulding.appspot.com/o/videos%2F05a88dbf-e5a9-4b8f-9fc6-3ecdb9aa1366?alt=media&token=40913e06-0769-4a85-a9cd-61dee4b19999";




    FirebaseFirestore  db= FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate_view);

        //next and prev button for fragment
        isFirst=true;
        edit=findViewById(R.id.point_evaluate_editText);
        next=findViewById(R.id.next_candidate);
        previous=findViewById(R.id.previous_candidate);
        save=findViewById(R.id.evaluation_save_btn);
        if(auth.getCurrentUser()!=null) {
            rvID=auth.getCurrentUser().getUid();

        }else {
            rvID = "xIwX5LPaEs1N0sehdDWg";
        }

        //load title start
        name=findViewById(R.id.comp_title);
        //get bundle key for comp id
        if(getIntent().getExtras()!=null){
            if (getIntent().getExtras().getString("competitionPath")!=null){
            competitionPath =getIntent().getExtras().getString("competitionPath");}
            else  competitionPath ="F1KOzoe56XhzzevycpRC";
        }else{
            competitionPath ="F1KOzoe56XhzzevycpRC";}
        //load url to player and create fragments, then add listener to previous and next button

        db.collection("ENTRY_LIST").whereEqualTo("COMP_ID",competitionPath)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                               List<DocumentSnapshot>l= task.getResult().getDocuments();
                                vid1=(String) l.get(0).getData().get("ENTRY_LINK");
                                f1= new Candidate1Fragment(vid1);
                                changeFragment(f1);
                                if (l.size()==2){
                                    vid2=(String) l.get(1).getData().get("ENTRY_LINK");
                                    f2= new Candidate2Fragment(vid2);
                                }
                                else{
                                    vid2="";
                                    f2= new Candidate2Fragment(vid2);
                                }

                            //        //add prev listener
                            previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFirst=true;
             fragmentManager.popBackStack();
                previous.setEnabled(false);
                next.setEnabled(true);

            }
        });
                            //add next button listener
                            next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFirst=false;
               changeFragment(f2);
                previous.setEnabled(true);
                next.setEnabled(false);

            }
        });

                            DocumentReference docRef = db.collection("COMPETITION2").document(competitionPath);
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    DocumentSnapshot document = task.getResult();
                                    name.setText((String)document.getData().get("title"));
                                    save.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (isFirst){
                                                current=(String) document.getData().get("compettionUser1");
                                            }
                                            else {
                                                current=(String) document.getData().get("compettionUser2");
                                            }

                                            CollectionReference ref= db.collection("REVIEW_LIST");
                                            ref.whereEqualTo("COMPETITION_ID",competitionPath)
                                                    .whereEqualTo("CANDIDATE_ID",current)
                                                    .whereEqualTo("USER_ID",rvID).get()
                                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                    if (task.getResult().getDocuments().size()==0){
                                                                        Map<String,String> rvm= new HashMap<>();
                                                                        rvm.put("CANDIDATE_ID",current);
                                                                        rvm.put("USER_ID",rvID);
                                                                        rvm.put("COMPETITION_ID",competitionPath);
                                                                        ref.document(UUID.randomUUID().toString()).set(rvm);

                                                                        db.collection("COMPETITION2").document(competitionPath)
                                                                                .update("totalVote",FieldValue.increment(1)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        Toast.makeText(EvaluateView.this,"Đã lưu",Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                });
                                                                        CollectionReference ref= db.collection("ENTRY_LIST");
                                                                        ref.whereEqualTo("COMPETITION_ID",competitionPath)
                                                                                .whereEqualTo("CANDIDATE_ID",current).get()
                                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                        if (task.isSuccessful()) {
                                                                                           // save.setEnabled(false);
                                                                                            save.setText("Đã lưu");
                                                                                            save.setEnabled(false);

                                                                                            for (QueryDocumentSnapshot document1 : task.getResult()) {
                                                                                                Map<Object, String> map = new HashMap<>();
                                                                                                String point=(String)document1.getData().get("POINT");
                                                                                                int intPoint= Integer.parseInt(point);
                                                                                                String pointe;
                                                                                                if (isFirst){
                                                                                                    pointe=  f1.getEditText();

                                                                                                }
                                                                                                else {
                                                                                                    pointe=  f2.getEditText();
                                                                                                }

                                                                                                int intPointEdit=Integer.parseInt(pointe);
                                                                                                String vote=(String)document1.getData().get("VOTE");
                                                                                                Integer i= Integer.parseInt(vote);

                                                                                                //int intPoint= Integer.parseInt(point);
                                                                                                map.put("POINT", Integer.toString(intPoint+intPointEdit));
                                                                                                map.put("VOTE", Integer.toString(i+1));
                                                                                                ref.document(document1.getId()).set(map, SetOptions.merge());
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                });
                                                                    }
                                                                    else {
                                                                        save.setText("Đã lưu");
                                                                        save.setEnabled(false);
                                                                        Toast.makeText(EvaluateView.this,"Đã đánh giá trước đó",Toast.LENGTH_SHORT).show();
                                                                    }

                                                                }
                                                            });
//                                                    .addOnFailureListener(new OnFailureListener() {
//                                                        @Override
//                                                        public void onFailure(@NonNull Exception e) {
//                                                            db.collection("COMPETITION2").document(competitionPath)
//                                                                    .update("totalVote",FieldValue.increment(1)).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                                        @Override
//                                                                        public void onComplete(@NonNull Task<Void> task) {
//                                                                            Toast.makeText(EvaluateView.this,"Đã lưu",Toast.LENGTH_SHORT).show();
//                                                                        }
//                                                                    });
//                                                            CollectionReference ref= db.collection("ENTRY_LIST");
//                                                            ref.whereEqualTo("COMPETITION_ID",competitionPath)
//                                                                    .whereEqualTo("CANDIDATE_ID",current).get()
//                                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                                                        @Override
//                                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                                                            if (task.isSuccessful()) {
//
//                                                                                for (QueryDocumentSnapshot document1 : task.getResult()) {
//                                                                                    Map<Object, String> map = new HashMap<>();
//                                                                                    String point=(String)document1.getData().get("POINT");
//                                                                                    int intPoint= Integer.parseInt(point);
//                                                                                    String pointe;
//                                                                                    if (isFirst){
//                                                                                        pointe=  f1.getEditText();
//                                                                                    }
//                                                                                    else {
//                                                                                        pointe=  f2.getEditText();
//                                                                                    }
//
//                                                                                    int intPointEdit=Integer.parseInt(pointe);
//                                                                                    String vote=(String)document1.getData().get("VOTE");
//                                                                                    Integer i= Integer.parseInt(vote);
//
//                                                                                    //int intPoint= Integer.parseInt(point);
//                                                                                    map.put("POINT", Integer.toString(intPoint+intPointEdit));
//                                                                                    map.put("VOTE", Integer.toString(i+1));
//                                                                                    ref.document(document1.getId()).set(map, SetOptions.merge());
//                                                                                }
//                                                                            }
//                                                                        }
//                                                                    });
//                                                        }
//                                                    });





                                        }
                                    });

                                }
                            });

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
//
//        DocumentReference docRef = db.collection("COMPETITION2").document(competitionPath);
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                DocumentSnapshot document = task.getResult();
//                name.setText((String)document.getData().get("title"));
//                save.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (isFirst){
//                            current=(String) document.getData().get("compettionUser1");
//                        }
//                        else {
//                            current=(String) document.getData().get("compettionUser2");
//                        }
//
//                        db.collection("COMPETITION2").document(competitionPath)
//                                .update("totalVote",FieldValue.increment(1)).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        Toast.makeText(EvaluateView.this,"Đã lưu",Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                        CollectionReference ref= db.collection("ENTRY_LIST");
//                        ref.whereEqualTo("COMPETITION_ID",competitionPath)
//                                .whereEqualTo("CANDIDATE_ID",current).get()
//                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                        if (task.isSuccessful()) {
//                                            for (QueryDocumentSnapshot document1 : task.getResult()) {
//                                                Map<Object, String> map = new HashMap<>();
//                                                String vote=(String)document1.getData().get("VOTE");
//                                                 int intVote= Integer.parseInt(vote);
//                                            //    String point=   edit.getText().toString();
//                                                //int intPoint= Integer.parseInt(point);
//                                                map.put("VOTE","3");
//                                                ref.document(document1.getId()).set(map, SetOptions.merge());
//                                            }
//                                        }
//                                    }
//                                });
//
//                    }
//                });
//
//            }
//        });
//        //add save btn event


    }

public void changeFragment(Fragment fragment){
        if (fragment!=null){
             fragmentManager= getSupportFragmentManager();
            FragmentTransaction ft=fragmentManager.beginTransaction();
            ft.replace(R.id.fragment_area,fragment);
            save.setEnabled(true);
            save.setText("Lưu");
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