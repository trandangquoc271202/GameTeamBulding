package vn.edu.hcmuaf.fit.gameteambulding.ui.viewresult;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import vn.edu.hcmuaf.fit.gameteambulding.Model.Competition;
import vn.edu.hcmuaf.fit.gameteambulding.R;
import vn.edu.hcmuaf.fit.gameteambulding.ui.viewresult.ViewResult;

public class ViewCompetition extends AppCompatActivity {
    private RecyclerView rcvCompetition;
    private CompetitionAdapter competitionAdapter;

    private static final String TAG = "ViewCompetition";
    private List<Competition> listCompetition;
    private LinearLayout linearLayoutCompetition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.management_competition);
        initUi();
    }

    private void initUi() {
        rcvCompetition = findViewById(R.id.recyclerViewCompetition);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvCompetition.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcvCompetition.addItemDecoration(dividerItemDecoration);
        listCompetition = new ArrayList<>();
        // Removed the call to getAllDataFromFirestore() here
        getAllDataFromFirestore();
        // Initialize the adapter here, but don't set it until you have the data
        competitionAdapter = new CompetitionAdapter(listCompetition);

        // Set the adapter after retrieving data
        rcvCompetition.setAdapter(competitionAdapter);

        // Call method to get data from Firestore and update UI

    }

    private void getAllDataFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("COMPETITION").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Competition competition = document.toObject(Competition.class);
                    if (competition != null) {
                        listCompetition.add(competition);
                    }
                }
                // Notify the adapter that the data set has changed
                competitionAdapter.notifyDataSetChanged();
                Log.w(TAG, "competitionList: " + listCompetition);
                // Now that you have the data, update your UI
            } else {
                Log.w(TAG, "Error getting documents.", task.getException());
            }
        });
    }


}
