package vn.edu.hcmuaf.fit.gameteambulding;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import vn.edu.hcmuaf.fit.gameteambulding.Firebase.CompetitionService;

public class CreateCompetitionActivity extends AppCompatActivity {
    private View back;
    private TextInputEditText title, timeStart, timeEnd, content, criteria, email;
    private Button createContest, addCriteria, addEmail, btn_del, btn_cancel;
    private FirebaseFirestore db;
    private String idCreator;
    private ArrayList<String> listCriteria, listEmail;
    private CriteriaAdapter criteriaAdapter;
    private EmailAdapter emailAdapter;
    private ListView lv_criteria, lv_email;
    private LinearLayout linearLayout, linearLayoutEmail;
    private String idEmail, idCriteria;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_competition);
        db = FirebaseFirestore.getInstance();
        back = (View) findViewById(R.id.back);
        addCriteria = (Button) findViewById(R.id.add_criteria);
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
                showTimePickerDialog(CreateCompetitionActivity.this);
            }
        });
        idCreator = "12345";
        content = (TextInputEditText) findViewById(R.id.edit_content);
        criteria = (TextInputEditText) findViewById(R.id.edit_criteria);
        email = (TextInputEditText) findViewById(R.id.edit_email);
        lv_criteria = (ListView) findViewById(R.id.lv_criteria);
        lv_email = (ListView) findViewById(R.id.lv_email);
        linearLayout = (LinearLayout) findViewById(R.id.list_criteria);
        linearLayoutEmail = (LinearLayout) findViewById(R.id.list_email);
        // Create ListView Criteria
        updateLV();
        listCriteria = new ArrayList<String>();
        listEmail = new ArrayList<String>();
        // End ListView Criteria


//
        lv_criteria.setOnItemLongClickListener((adapterView, view, i, l) -> {
            deleteCriteria(i);
            return true;
        });
        lv_email.setOnItemLongClickListener((adapterView, view, i, l) -> {
            deleteEmail(i);
            return true;
        });
//
        addCriteria();
        addEmail();
        addCriteriaInFirebase();
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

    public void deleteCriteria(int i) {
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

                listCriteria.remove(i);

                criteriaAdapter.notifyDataSetChanged();
                updateLinearLayoutHeight();
                dialog.dismiss();
            }
        });
        dialog.show();
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

    public void addCriteria() {
        addCriteria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listCriteria.size() <= 2 && !criteria.getText().toString().equals("")) {
                    listCriteria.add(criteria.getText().toString());
                    criteriaAdapter = new CriteriaAdapter(getApplicationContext(), CreateCompetitionActivity.this, listCriteria);
                    lv_criteria.setAdapter(criteriaAdapter);
                    updateLinearLayoutHeight();
                    criteria.setText("");
                }
            }
        });
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

    public void updateLV() {
        listCriteria = new ArrayList<String>();
        criteriaAdapter = new CriteriaAdapter(getApplicationContext(), CreateCompetitionActivity.this, listCriteria);
        lv_criteria.setAdapter(criteriaAdapter);
    }

    public void createCompetition(String idCriteria, String idEmail) {
        Map<String, Object> competition = new HashMap<>();
        competition.put("TITLE", title.getText().toString());
        competition.put("CREATOR", idCreator);
        competition.put("TIME_START", timeStart.getText().toString());
        competition.put("TIME_END", timeEnd.getText().toString());
        competition.put("CONTENT", content.getText().toString());
        competition.put("CRITERIALIST", idCriteria);
        competition.put("EMAILLIST", idEmail);
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

    public void addCriteriaInFirebase() {
        createContest = (Button) findViewById(R.id.createCompetition);

        createContest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> competition = new HashMap<>();
                for (int i = 0; i < listCriteria.size(); i++) {
                    competition.put("CRITERIA" + i, listCriteria.get(i));
                }

                db.collection("CRITERIALIST")
                        .add(competition)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                addEmailInFirebase(documentReference.getId());
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

    public void addEmailInFirebase(String idCriteria) {
        Map<String, Object> competition = new HashMap<>();
        for (int i = 0; i < listEmail.size(); i++) {
            competition.put("EMAIL" + i, listEmail.get(i));
        }

        db.collection("EMAILLIST")
                .add(competition)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        createCompetition(idCriteria, documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    private void updateLinearLayoutHeight() {
        int totalHeight = 300;
        for (int i = 0; i < criteriaAdapter.getCount(); i++) {
            View listItem = criteriaAdapter.getView(i, null, lv_criteria);
            listItem.measure(
                    View.MeasureSpec.makeMeasureSpec(lv_criteria.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            );
            totalHeight += listItem.getMeasuredHeight();
        }

        // Thêm chiều cao của các đường phân cách giữa các phần tử
        totalHeight += (lv_criteria.getDividerHeight() * (criteriaAdapter.getCount() - 1));

        // Lấy LayoutParams của LinearLayout và đặt chiều cao mới
        ViewGroup.LayoutParams params = linearLayout.getLayoutParams();
        params.height = totalHeight;
        lv_criteria.setLayoutParams(params);
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
    private void showTimePickerDialog(Context context) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                context,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Xử lý giờ được chọn ở đây
                        String selectedTime = hourOfDay + ":" + minute;
                        // Hiển thị giờ đã chọn trên nút hoặc nơi khác
                        timeEnd.setText(selectedTime);
                    }
                },
                hour,
                minute,
                true // true nếu sử dụng định dạng 24 giờ, false nếu sử dụng định dạng 12 giờ
        );

        // Hiển thị hộp thoại chọn giờ
        timePickerDialog.show();
    }
}