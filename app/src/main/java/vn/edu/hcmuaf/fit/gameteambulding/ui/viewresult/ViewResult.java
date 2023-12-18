package vn.edu.hcmuaf.fit.gameteambulding.ui.viewresult;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.List;

import vn.edu.hcmuaf.fit.gameteambulding.Model.Competition;
import vn.edu.hcmuaf.fit.gameteambulding.Model.CompetitionUser;
import vn.edu.hcmuaf.fit.gameteambulding.Model.UserInfo;
import vn.edu.hcmuaf.fit.gameteambulding.R;

public class ViewResult extends AppCompatActivity {

    private View back;
    TextView view_detail1, view_detail2;
    Competition competition;
    TextView timeStart, timeEnd, name1, name2, totalPoint1, totalPoint2, textViewResult1, textViewResult2;
    ImageView img1, img2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // log
        Log.d("ViewResult", "onCreate: ");
        setContentView(R.layout.activity_result_exam);
        timeStart = findViewById(R.id.textViewStart);
        timeEnd = findViewById(R.id.textTimeEnd);
        name1 = findViewById(R.id.textViewName1);
        name2 = findViewById(R.id.textViewName2);
        totalPoint1 = findViewById(R.id.textViewTotalPoint1);
        totalPoint2 = findViewById(R.id.textViewTotalPoint2);
        img1 = findViewById(R.id.imageView1);
        img2 = findViewById(R.id.imageView2);
        textViewResult1 = findViewById(R.id.textViewResult1);
        textViewResult2 = findViewById(R.id.textViewResult2);
        back = findViewById(R.id.back);
        view_detail1 = findViewById(R.id.view_detail1);
        view_detail2 = findViewById(R.id.view_detail2);
        back();
        initializeFirebase();

        Bundle bundle = getIntent().getExtras();

        if (bundle == null) return;

        if (getIntent().hasExtra("COMPETITION")) {
            Competition competitionId = (Competition) bundle.getSerializable("COMPETITION");
            Log.i("ViewResult", "onCreate dsa dsads: " + competitionId);
            if (competitionId != null) {
                List<String> aa = Arrays.asList(competitionId.getCompettionUser1(), competitionId.getCompettionUser2());
                fetchAndSetCompetitionUserInformation(aa, competitionId);
            } else {
                Log.e("ViewResult", "Competition object is null");
            }
        }


    }


    private void initializeFirebase() {
        // No change needed, already correct
        FirebaseFirestore.getInstance(); // Initialize Firebase
    }

    private void fetchAndSetCompetitionUserInformation(List<String> userIds, Competition competition) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("COMPETITION_USER").whereIn(FieldPath.documentId(), userIds).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();

                if (querySnapshot != null) {
                    DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                    DocumentSnapshot document2 = querySnapshot.getDocuments().get(1);
                    CompetitionUser user1 = document.toObject(CompetitionUser.class);
                    CompetitionUser user2 = document2.toObject(CompetitionUser.class);

                    if (user1 != null) {
                        user1.setDocumentId(document.getId());
                        competition.setUser1(user1);
                    }
                    if (user2 != null) {

                        user2.setDocumentId(document2.getId());
                        competition.setUser2(user2);
                    }

                    Log.i("ViewResult", "fetchAndSetUserInformation3: " + competition);

                    if (competition.getUser1() != null && competition.getUser2() != null) {
                        List<String> aa = Arrays.asList(competition.getUser1().getUserId(), competition.getUser2().getUserId());
                        fetchAndSetUserInformation(aa, competition);
                        Log.i("ViewResult", "fetchAndSetUserInformation4: " + competition);
                    }
                }
            } else {
                Log.w("ViewResult", "Error getting user documents.", task.getException());
            }
        });
    }

    private void fetchAndSetUserInformation(List<String> userIds, Competition competition) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("USER2").whereIn(FieldPath.documentId(), userIds).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();

                if (querySnapshot != null) {
                    DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                    DocumentSnapshot document2 = querySnapshot.getDocuments().get(1);
                    UserInfo user1 = document.toObject(UserInfo.class);
                    UserInfo user2 = document2.toObject(UserInfo.class);

                    if (user1 != null) competition.getUser1().setUserInfo(user1);
                    if (user2 != null) competition.getUser2().setUserInfo(user2);

                    if (competition.getUser1().getUserInfo() != null && competition.getUser2().getUserInfo() != null) {
                        timeStart.setText("Thời gian bắt đầu: " + competition.getStartAt());
                        timeEnd.setText("Thời gian kết thúc: " + competition.getEndAt());
                        totalPoint1.setText("Tổng kết điểm: " + competition.getUser1().getTotalScore() + "");
                        name1.setText("Họ và Tên: " + competition.getUser1().getUserInfo().getUsername());
                        name2.setText("Họ và Tên: " + competition.getUser2().getUserInfo().getUsername());
                        totalPoint2.setText("Tổng kết điểm: " + competition.getUser2().getTotalScore() + "");
                        textViewResult1.setText("Kết quả: " + competition.getUser1().getResult());
                        textViewResult2.setText("Kết quả: " + competition.getUser2().getResult());
                        img1.setImageResource(competition.getUser1().getResult().equalsIgnoreCase("Thua") ? R.drawable.lose : R.drawable.win);
                        img2.setImageResource(competition.getUser2().getResult().equalsIgnoreCase("Thua") ? R.drawable.lose : R.drawable.win);
                        view_detail1.setOnClickListener(view -> {
                            Intent intent = new Intent(ViewResult.this, ViewResultDetail.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("USER2", competition);
                            bundle.putString("USER_ID", "USER1");
                            intent.putExtras(bundle);
                            startActivity(intent);
                        });
                        view_detail2.setOnClickListener(view -> {
                            Intent intent = new Intent(ViewResult.this, ViewResultDetail.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("USER2", competition);
                            bundle.putString("USER_ID", "USER2");
                            intent.putExtras(bundle);
                            startActivity(intent);
                        });
                    }
                }
            } else {
                Log.w("ViewResult", "Error getting user documents.", task.getException());
            }
        });
    }


    private void back() {
        back.setOnClickListener(view -> finish());
    }
}

