package vn.edu.hcmuaf.fit.gameteambulding;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

import vn.edu.hcmuaf.fit.gameteambulding.Model.Contest;

public class DetailContestActivity extends AppCompatActivity {
    private View back;
    private TextInputEditText title, timeStart, timeEnd, content, criteria, email;
    private Button addEmail, btn_del, btn_cancel, update, delete;
    private FirebaseFirestore db;
    private String idCreator;
    private ArrayList<String> listCriteria, listEmail;
    private CriteriaContestAdapter criteriaAdapter;
    private EmailContestAdapter emailAdapter;
    private ListView lv_criteria, lv_email;
    private LinearLayout linearLayout, linearLayoutEmail;
    private String idEmail, idCriteria, idContest;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_contest);
        Intent intent = getIntent();
        idContest = intent.getStringExtra("id");
        db = FirebaseFirestore.getInstance();
        back = (View) findViewById(R.id.back);
        addEmail = (Button) findViewById(R.id.add_email);
        title = (TextInputEditText) findViewById(R.id.edit_text_title);
        timeStart = (TextInputEditText) findViewById(R.id.editDateStart);
        timeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(DetailContestActivity.this);
            }
        });
        timeEnd = (TextInputEditText) findViewById(R.id.editDateEnd);
        timeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialogEnd(DetailContestActivity.this);
            }
        });
        idCreator = "12345";
        content = (TextInputEditText) findViewById(R.id.edit_content);

        email = (TextInputEditText) findViewById(R.id.edit_email);

        lv_email = (ListView) findViewById(R.id.lv_email);

        linearLayoutEmail = (LinearLayout) findViewById(R.id.list_email);
        update = findViewById(R.id.updateContest);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDialog();
            }
        });
        delete = findViewById(R.id.deleteContest);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog();
            }
        });
        // Create ListView Criteria

        listCriteria = new ArrayList<String>();
        listEmail = new ArrayList<String>();

        lv_email.setOnItemLongClickListener((adapterView, view, i, l) -> {
            deleteEmail(i);
            return true;
        });
        addEmail();
        loadContest();
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
        dialog = new Dialog(DetailContestActivity.this);
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
                    emailAdapter = new EmailContestAdapter(getApplicationContext(), DetailContestActivity.this, listEmail);
                    lv_email.setAdapter(emailAdapter);
                    updateLinearLayoutHeightEmail();
                    email.setText("");
                }
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





    public void loadContest() {
        DocumentReference documentReference = db.collection("COMPETITION2").document(idContest);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // Dữ liệu của collection
                    DocumentSnapshot documentSnapshot = task.getResult();
                    title.setText(documentSnapshot.getString("title"));
                    timeStart.setText(documentSnapshot.getString("startAt"));
                    timeEnd.setText(documentSnapshot.getString("endAt"));
                    content.setText(documentSnapshot.getString("content"));
                    loadUser(documentSnapshot.getString("compettionUser1"), documentSnapshot.getString("compettionUser2"));
                } else {

                }
            }
        });
    }


    public void loadUser(String id1, String id2) {
        DocumentReference documentReference = db.collection("COMPETITION_USER").document(id1);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // Dữ liệu của collection
                    DocumentSnapshot documentSnapshot = task.getResult();
                    String idUser = documentSnapshot.getString("userId");

                    DocumentReference documentReference = db.collection("Users").document(idUser);
                    documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                String email = documentSnapshot.getString("email");
                                loadUser2(email, id2);
                            } else {

                            }
                        }
                    });

                } else {

                }
            }
        });
    }

    public void loadUser2(String email1, String id) {
        DocumentReference documentReference = db.collection("COMPETITION_USER").document(id);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // Dữ liệu của collection
                    DocumentSnapshot documentSnapshot = task.getResult();
                    String idUser = documentSnapshot.getString("userId");

                    DocumentReference documentReference = db.collection("Users").document(idUser);
                    documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                String email = documentSnapshot.getString("email");
                                listEmail.add(email1);
                                listEmail.add(email);
                                emailAdapter = new EmailContestAdapter(getApplicationContext(), DetailContestActivity.this, listEmail);
                                lv_email.setAdapter(emailAdapter);
                                updateLinearLayoutHeightEmail();
                            } else {

                            }
                        }
                    });

                } else {

                }
            }
        });
    }

    public void deleteDialog() {
        dialog = new Dialog(DetailContestActivity.this);
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
                DocumentReference documentReference = db.collection("COMPETITION2").document(idContest);
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                             DocumentSnapshot documentSnapshot = task.getResult();
                                deleteCriteriaAndEmail(documentSnapshot.getString("compettionUser1"), documentSnapshot.getString("compettionUser2"));
                                Toast.makeText(getApplicationContext(), "Xóa thành công!", Toast.LENGTH_SHORT).show();

                        } else {

                        }
                    }
                });
                dialog.dismiss();
                finish();
            }
        });
        dialog.show();
    }

    public void deleteCriteriaAndEmail(String idC, String idE) {
        DocumentReference documentReference = db.collection("COMPETITION2").document(idContest);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                } else {

                }
            }
        });
        documentReference = db.collection("COMPETITION_USER").document(idE);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                } else {

                }
            }
        });
        documentReference = db.collection("COMPETITION_USER").document(idC);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                } else {

                }
            }
        });
    }

    public void updateDialog() {
        dialog = new Dialog(DetailContestActivity.this);
        dialog.setContentView(R.layout.dialog_del);
        btn_cancel = dialog.findViewById(R.id.btn_cancel);
        btn_del = dialog.findViewById(R.id.btn_del);
        btn_del.setText("Cập nhật");
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateContestFinal();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void updateCriteria(String id) {
        DocumentReference documentReference = db.collection("CRITERIALIST").document(id);
        Map<String, Object> data = new HashMap<>();
        for (int i = 0; i < listCriteria.size(); i++) {
            data.put("CRITERIA" + i, listCriteria.get(i));
        }
        documentReference.update(data);

    }

    public void updateEmail(String id) {
        DocumentReference documentReference = db.collection("COMPETITION_USER").document(id);
        Map<String, Object> data = new HashMap<>();
        for (int i = 0; i < listEmail.size(); i++) {
            data.put("EMAIL" + i, listEmail.get(i));
        }
        documentReference.update(data);
    }

    public void updateContest(String id1, String id2) {
        DocumentReference documentReference = db.collection("COMPETITION2").document(idContest);
        Map<String, Object> competition = new HashMap<>();
        competition.put("title", title.getText().toString());
        competition.put("creator", idCreator);
        competition.put("startAt", timeStart.getText().toString());
        competition.put("endAt", timeEnd.getText().toString());
        competition.put("content", content.getText().toString());
        competition.put("totalVote", "");
        competition.put("compettionUser1", id1);
        competition.put("compettionUser2", id2);
        documentReference.update(competition);
    }

    public void getId(String idCompetitionUser, String email) {
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
                                if (document.getData().get("email").toString().equals(email)) {
                                    competitionUser1(document.getId(), idCompetitionUser);
                                    break;
                                }
                            }

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void competitionUser1(String idUser, String idCompetitionUser) {

        DocumentReference documentReference = db.collection("COMPETITION_USER").document(idCompetitionUser);
        Map<String, Object> competition = new HashMap<>();
        competition.put("userId", idUser);
        competition.put("totalScore", "");
        competition.put("result", "");
        competition.put("totalVote", "");
        documentReference.update(competition);
    }
    public void getId2(String idCompetitionUser,  String email) {
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
                                if (document.getData().get("email").toString().equals(email)) {
                                    competitionUser2(document.getId(), idCompetitionUser);
                                    break;
                                }
                            }

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void competitionUser2(String idUser, String idCompetitionUser) {
        DocumentReference documentReference = db.collection("COMPETITION_USER").document(idCompetitionUser);
        Map<String, Object> competition = new HashMap<>();
        competition.put("userId", idUser);
        competition.put("totalScore", "");
        competition.put("result", "");
        competition.put("totalVote", "");
        documentReference.update(competition);
    }

    public void updateContestFinal() {
        DocumentReference documentReference = db.collection("COMPETITION2").document(idContest);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if ((listEmail.size() == 2) && !title.getText().toString().equals("") && !timeEnd.getText().toString().equals("")
                            && !timeStart.getText().toString().equals("") && !content.getText().toString().equals("")) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        getId(documentSnapshot.getString("compettionUser1"),listEmail.get(0));
                        getId2(documentSnapshot.getString("compettionUser2"), listEmail.get(1));
                        updateContest(documentSnapshot.getString("compettionUser1"), documentSnapshot.getString("compettionUser2"));
                        Toast.makeText(getApplicationContext(), "Cập nhật thành công!!", Toast.LENGTH_SHORT).show();
                    }
                } else {

                }
            }
        });
    }
}