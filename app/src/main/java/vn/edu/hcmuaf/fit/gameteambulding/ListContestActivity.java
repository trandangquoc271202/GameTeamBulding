package vn.edu.hcmuaf.fit.gameteambulding;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import vn.edu.hcmuaf.fit.gameteambulding.Model.Contest;

public class ListContestActivity extends AppCompatActivity {
    private ArrayList<Contest> listContest;
    ContestAdapter adapter;
    ListView lv_main;
    private String idUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_contest);
        idUser ="12345";
        listContest = new ArrayList<Contest>();
        lv_main = findViewById(R.id.listContest);
        lv_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ListContestActivity.this, DetailContestActivity.class);
                intent.putExtra("id", listContest.get(i).getId());
                startActivity(intent);
            }
        });
        loadContest();
    }
    public void loadContest(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("COMPETITION")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Contest contest;
                        if (task.isSuccessful()) {
                            ArrayList<Contest> list = new ArrayList<Contest>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getData().get("CREATOR").toString().equals(idUser)){
                                    contest = new Contest(document.getId(), document.getData().get("TITLE").toString());
                                    list.add(contest);
//                                    Toast.makeText(getApplicationContext(), "Contest: "+list.get(0).getId(), Toast.LENGTH_SHORT).show();
                                }

                            }
                            UpdateLV(list);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    public void UpdateLV(ArrayList<Contest> list) {
        listContest = (ArrayList<Contest>) list;
        adapter = new ContestAdapter(getApplicationContext(), ListContestActivity.this, list);
        lv_main.setAdapter(adapter);
    }
}