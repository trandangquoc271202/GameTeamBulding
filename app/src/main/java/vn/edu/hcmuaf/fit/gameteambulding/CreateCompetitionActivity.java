package vn.edu.hcmuaf.fit.gameteambulding;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vn.edu.hcmuaf.fit.gameteambulding.Firebase.CompetitionService;

public class CreateCompetitionActivity extends AppCompatActivity {
    private View back;
    private TextInputEditText title, timeStart, timeEnd, content, criteria;
    private Button createContest, addCriteria;
    private CompetitionService competitionService;
    private FirebaseFirestore db;
    private String idCreator;
    private ArrayList<String> listCriteria;
    private CriteriaAdapter criteriaAdapter;
    private ListView lv_criteria;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_competition);
        db = FirebaseFirestore.getInstance();
        back = (View) findViewById(R.id.back);
        addCriteria = (Button) findViewById(R.id.add_criteria);
        title = (TextInputEditText) findViewById(R.id.edit_text_title);
        timeStart = (TextInputEditText) findViewById(R.id.editDateStart);
        timeEnd = (TextInputEditText) findViewById(R.id.editDateEnd);
        idCreator = "12345";
        content = (TextInputEditText) findViewById(R.id.edit_content);
        criteria = (TextInputEditText) findViewById(R.id.edit_criteria);
        lv_criteria = (ListView) findViewById(R.id.lv_criteria);
        updateLV();
        // Create ListView Criteria

        listCriteria = new ArrayList<String>();
        // End ListView Criteria
        addCriteria();
        createCompetition();
        back();
    }

    public void back() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void addCriteria() {
        addCriteria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listCriteria.add(criteria.getText().toString());
                criteriaAdapter = new CriteriaAdapter(getApplicationContext(), CreateCompetitionActivity.this, listCriteria);
                lv_criteria.setAdapter(criteriaAdapter);
//                updateLinearLayoutHeight();
            }
        });
    }
    public void updateLV() {
        listCriteria = new ArrayList<String>();
        criteriaAdapter = new CriteriaAdapter(getApplicationContext(), CreateCompetitionActivity.this, listCriteria);
        lv_criteria.setAdapter(criteriaAdapter);
    }
    public void createCompetition() {

        createContest = (Button) findViewById(R.id.createCompetition);

        createContest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> competition = new HashMap<>();
                competition.put("TITLE", title.getText().toString());
                competition.put("CREATOR", idCreator);
                competition.put("TIME_START", timeStart.getText().toString());
                competition.put("TIME_END", timeEnd.getText().toString());
                competition.put("CONTENT", content.getText().toString());


                // Add a new document with a generated ID
                db.collection("COMPETITION")
                        .add(competition)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
            }
        });


    }
    private void updateLinearLayoutHeight() {
        int totalHeight = 0;

        // Kiểm tra null và chỉ tính chiều cao nếu view đã được vẽ
        for (int i = 0; i < criteriaAdapter.getCount(); i++) {
            View childView = lv_criteria.getChildAt(i);
            if (childView != null) {
                totalHeight += childView.getMeasuredHeight();
            }
        }

        // Lấy LayoutParams của LinearLayout và đặt chiều cao mới
        ViewGroup.LayoutParams params = lv_criteria.getLayoutParams();
        params.height = totalHeight + (lv_criteria.getDividerHeight() * (criteriaAdapter.getCount() - 1))+80;
        lv_criteria.setLayoutParams(params);
    }
}