package vn.edu.hcmuaf.fit.gameteambulding.ui.viewresult;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.List;

import vn.edu.hcmuaf.fit.gameteambulding.Model.Competition;
import vn.edu.hcmuaf.fit.gameteambulding.Model.CompetitionReviewUser;
import vn.edu.hcmuaf.fit.gameteambulding.Model.CompetitionUser;
import vn.edu.hcmuaf.fit.gameteambulding.Model.UserInfo;
import vn.edu.hcmuaf.fit.gameteambulding.R;

public class ViewResultDetail extends AppCompatActivity {
    TextView textViewName, valueTopic, labelContent, labelTotalScore, labelTotalVote, valueView;
    private View back;
    LinearLayout linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_result);
        textViewName = findViewById(R.id.textViewName);
        valueTopic = findViewById(R.id.valueTopic);
        labelContent = findViewById(R.id.labelContent);
        labelTotalScore = findViewById(R.id.valueTotalScore);
        labelTotalVote = findViewById(R.id.valueTotalVote);
        valueView = findViewById(R.id.valueView);
        linear = findViewById(R.id.linearCriteria);
        back = (View) findViewById(R.id.back);
        back();


        Bundle bundle = getIntent().getExtras();
        Log.i("huudan", "onCreate: " + bundle);
        if (bundle != null && bundle.containsKey("USER2")) {

            Competition competition = (Competition) bundle.getSerializable("USER2");
            Log.i("huudan", "compee: " + competition);
            if (competition != null) {
                Log.i("huudan", "user2: " + competition.getUser2());
                if (bundle != null && bundle.containsKey("USER_ID")) {

                    String userId = (String) bundle.getString("USER_ID");
                    Log.i("huudan", "userId: " + userId);
                    if (userId != null) {
                        getAllDataFromFirestore(userId, competition);
                    }
                }
            }
        }

    }

    private void getAllDataFromFirestore(String userId, Competition competition) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String documentUserId = userId.equals("USER2") ? competition.getUser2().getDocumentId() : competition.getUser1().getDocumentId();
        Log.i("huudan", "documentUserId: " + documentUserId);
        // Use a collection reference directly to avoid confusion
        db.collection("CompetitionReviewUser")
                .whereEqualTo("CompetitionUserId", documentUserId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> documents = task.getResult().getDocuments();
                        Log.i("huudan", "documents " + documents);
                        if (!documents.isEmpty()) {
                            for (DocumentSnapshot documentSnapshot : documents) {
                                Log.i("huudan", "documentSnapshot " + documentSnapshot.getId());
                                if (documentSnapshot.exists()) {
                                    CompetitionReviewUser competitionReviewUser = documentSnapshot.toObject(CompetitionReviewUser.class);
                                    Log.i("huudan", "competitionReviewUser " + competitionReviewUser);
                                    if (userId.equals("USER2") && competition.getUser2() != null) {
                                        competition.getUser2().add(competitionReviewUser);
                                        textViewName.setText(competition.getUser2().getUserInfo().getUsername());
                                        Log.i("huudan", "onCreate:11 " + competition.getUser2().getUserInfo().getUsername());
                                        valueTopic.setText(competition.getTitle());
                                        Log.i("huudan", "chủ đề " + competition.getTitle());
                                        labelContent.setText(competition.getContent());
                                        labelTotalScore.setText(competition.getUser2().totalScore());
                                        labelTotalVote.setText(competition.getUser2().totalVote());

                                        if (competition.getUser2().getCompetitionReviewUsers().isEmpty()) {
                                            valueView.setText("0");
                                        } else {
                                            for (CompetitionReviewUser competitionReviewUser1 : competition.getUser2().getCompetitionReviewUsers()
                                            ) {

                                                String name = competitionReviewUser1.getName();
                                                String value = String.valueOf(competitionReviewUser1.getScore());

                                                LinearLayout criteriaLayout = new LinearLayout(this);
                                                criteriaLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                                ));
                                                criteriaLayout.setOrientation(LinearLayout.HORIZONTAL);

                                                // Tạo TextView cho nhãn (label)
                                                TextView labelTextView = new TextView(this);
                                                labelTextView.setLayoutParams(new LinearLayout.LayoutParams(
                                                        130, // Đổi kích thước theo ý của bạn
                                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                                ));
                                                TextView valueTextView = new TextView(this);
                                                valueTextView.setLayoutParams(new LinearLayout.LayoutParams(
                                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                                ));

                                                labelTextView.setText(name);
                                                valueTextView.setText(value);
                                                criteriaLayout.addView(labelTextView);
                                                criteriaLayout.addView(valueTextView);
                                                linear.addView(criteriaLayout);


                                            }
                                        }
                                        valueView.setText(competition.getUser2().getResult());
                                    }
                                    if (userId.equals("USER1") && competition.getUser1() != null) {
                                        competition.getUser1().add(competitionReviewUser);
                                        textViewName.setText(competition.getUser1().getUserInfo().getUsername());
                                        valueTopic.setText(competition.getTitle());
                                        labelContent.setText(competition.getContent());
                                        labelTotalScore.setText(competition.getUser1().totalScore());
                                        labelTotalVote.setText(competition.getUser1().totalVote());
                                        valueView.setText(competition.getUser1().getResult());

                                        for (CompetitionReviewUser competitionReviewUser1 : competition.getUser1().getCompetitionReviewUsers()
                                        ) {

                                            String name = competitionReviewUser1.getName();
                                            String value = String.valueOf(competitionReviewUser1.getScore());

                                            LinearLayout criteriaLayout = new LinearLayout(this);
                                            criteriaLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                            ));
                                            criteriaLayout.setOrientation(LinearLayout.HORIZONTAL);

                                            // Tạo TextView cho nhãn (label)
                                            TextView labelTextView = new TextView(this);
                                            labelTextView.setLayoutParams(new LinearLayout.LayoutParams(
                                                    130, // Đổi kích thước theo ý của bạn
                                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                            ));
                                            TextView valueTextView = new TextView(this);
                                            valueTextView.setLayoutParams(new LinearLayout.LayoutParams(
                                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                            ));

                                            labelTextView.setText(name);
                                            valueTextView.setText(value);
                                            criteriaLayout.addView(labelTextView);
                                            criteriaLayout.addView(valueTextView);
                                            linear.addView(criteriaLayout);


                                        }

                                    }

// DO
                                } else {
                                    Log.w("ViewResult", "Document not found.");
                                }
                            }

                        } else {
                            Log.w("ViewResult", "No documents found.");
                        }
                    } else {
                        Log.w("ViewResult", "Error getting user documents.", task.getException());
                        // Handle the error, e.g., show an error message to the user
                        // handleError(task.getException());
                    }
                });
    }


    public void back() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
