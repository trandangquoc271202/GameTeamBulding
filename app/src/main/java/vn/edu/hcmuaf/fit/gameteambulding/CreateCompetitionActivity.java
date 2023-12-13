package vn.edu.hcmuaf.fit.gameteambulding;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import vn.edu.hcmuaf.fit.gameteambulding.Firebase.CompetitionService;
import vn.edu.hcmuaf.fit.gameteambulding.Model.Contest;

public class CreateCompetitionActivity extends AppCompatActivity {
    private View back;
    private TextInputEditText title, timeStart, timeEnd, content, email;
    private Button createContest, addEmail, btn_del, btn_cancel;
    private FirebaseFirestore db;
    private String idCreator;
    private ArrayList<String> listCriteria, listEmail;
    private CriteriaAdapter criteriaAdapter;
    private EmailAdapter emailAdapter;
    private ListView lv_email;
    private LinearLayout linearLayoutEmail;
    private String idEmail, idCriteria;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_competition);
        db = FirebaseFirestore.getInstance();
        back = (View) findViewById(R.id.back);
        createContest = (Button) findViewById(R.id.createCompetition);
        addEmail = (Button) findViewById(R.id.add_email);
        title = (TextInputEditText) findViewById(R.id.edit_text_title);
        timeStart = (TextInputEditText) findViewById(R.id.editDateStart);
        timeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(CreateCompetitionActivity.this);
            }
        });
        timeEnd = (TextInputEditText) findViewById(R.id.editDateEnd);
        timeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialogEnd(CreateCompetitionActivity.this);
            }
        });
        idCreator = "12345";
        content = (TextInputEditText) findViewById(R.id.edit_content);

        email = (TextInputEditText) findViewById(R.id.edit_email);

        lv_email = (ListView) findViewById(R.id.lv_email);

        linearLayoutEmail = (LinearLayout) findViewById(R.id.list_email);
        // Create ListView Criteria
        listCriteria = new ArrayList<String>();
        listEmail = new ArrayList<String>();
        // End ListView Criteria


//

        lv_email.setOnItemLongClickListener((adapterView, view, i, l) -> {
            deleteEmail(i);
            return true;
        });
//
        addEmail();
        getId1();

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


    public void deleteEmail(int i) {
        dialog = new Dialog(CreateCompetitionActivity.this);
        dialog.setContentView(R.layout.dialog_del);
        btn_cancel = dialog.findViewById(R.id.btn_cancel);
        btn_del = dialog.findViewById(R.id.btn_del);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listEmail.remove(i);

                emailAdapter.notifyDataSetChanged();
                updateLinearLayoutHeightEmail();
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public void addEmail() {
        addEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listEmail.size() <= 1 && !email.getText().toString().equals("")) {
                    listEmail.add(email.getText().toString());
                    emailAdapter = new EmailAdapter(getApplicationContext(), CreateCompetitionActivity.this, listEmail);
                    lv_email.setAdapter(emailAdapter);
                    updateLinearLayoutHeightEmail();
                    email.setText("");
                }
            }
        });
    }

    public void getId1() {
        createContest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                db.collection("Users")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                Contest contest;
                                if (task.isSuccessful()) {
                                    ArrayList<Contest> list = new ArrayList<Contest>();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (document.getData().get("email").toString().equals(listEmail.get(0))) {
                                            competitionUser1(document.getId());
                                            break;
                                        }
                                    }

                                } else {
                                    Log.w(TAG, "Error getting documents.", task.getException());
                                }
                            }
                        });
            }
        });

    }

    public void getId2(String idCompetitionUser1) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Contest contest;
                        if (task.isSuccessful()) {
                            ArrayList<Contest> list = new ArrayList<Contest>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getData().get("email").toString().equals(listEmail.get(1))) {
                                    competitionUser2(document.getId(), idCompetitionUser1);
                                    break;
                                }
                            }

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    public void createCompetition(String id1, String id2) {

        Map<String, Object> competition = new HashMap<>();
        competition.put("title", title.getText().toString());
        competition.put("creator", idCreator);
        competition.put("startAt", timeStart.getText().toString());
        competition.put("endAt", timeEnd.getText().toString());
        competition.put("content", content.getText().toString());
        competition.put("totalVote", "");
        competition.put("compettionUser1", id1);
        competition.put("compettionUser2", id2);
        // Add a new document with a generated ID
        db.collection("COMPETITION2")
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


    public void competitionUser1(String idUser) {
        Map<String, Object> competition = new HashMap<>();
        competition.put("userId", idUser);
        competition.put("totalScore", "");
        competition.put("result", "");
        competition.put("totalVote", "");

        db.collection("COMPETITION_USER")
                .add(competition)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        getId2(documentReference.getId());
//                                competitionUser2(documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

    }

    public void competitionUser2(String idUser, String idCompetitionUser1) {
        Map<String, Object> competition = new HashMap<>();
        competition.put("userId", idUser);
        competition.put("totalScore", "");
        competition.put("result", "");
        competition.put("totalVote", "");

        db.collection("COMPETITION_USER")
                .add(competition)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        createCompetition(documentReference.getId(), idCompetitionUser1);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    private void updateLinearLayoutHeightEmail() {
        int totalHeight = 300;
        for (int i = 0; i < emailAdapter.getCount(); i++) {
            View listItem = emailAdapter.getView(i, null, lv_email);
            listItem.measure(
                    View.MeasureSpec.makeMeasureSpec(lv_email.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            );
            totalHeight += listItem.getMeasuredHeight();
        }

        // Thêm chiều cao của các đường phân cách giữa các phần tử
        totalHeight += (lv_email.getDividerHeight() * (emailAdapter.getCount() - 1));

        // Lấy LayoutParams của LinearLayout và đặt chiều cao mới
        ViewGroup.LayoutParams params = linearLayoutEmail.getLayoutParams();
        params.height = totalHeight;
        lv_email.setLayoutParams(params);
    }

    private void showDatePickerDialog(Context context) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Xử lý ngày được chọn ở đây
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        // Hiển thị ngày đã chọn trên nút hoặc nơi khác
                        timeStart.setText(selectedDate);
                    }
                },
                year,
                month,
                day
        );

        // Hiển thị hộp thoại chọn ngày tháng năm
        datePickerDialog.show();
    }


    private void showDatePickerDialogEnd(Context context) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Xử lý ngày được chọn ở đây
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        // Hiển thị ngày đã chọn trên nút hoặc nơi khác
                        timeEnd.setText(selectedDate);
                    }
                },
                year,
                month,
                day
        );

        // Hiển thị hộp thoại chọn ngày tháng năm
        datePickerDialog.show();
    }


}