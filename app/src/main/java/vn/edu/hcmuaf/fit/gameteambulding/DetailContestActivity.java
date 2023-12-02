package vn.edu.hcmuaf.fit.gameteambulding;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DetailContestActivity extends AppCompatActivity {
    private View back;
    private TextInputEditText title, timeStart, timeEnd, content, criteria, email;
    private Button createContest, addCriteria, addEmail, btn_del, btn_cancel, update, delete;
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
        addCriteria = (Button) findViewById(R.id.add_criteria);
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
        criteria = (TextInputEditText) findViewById(R.id.edit_criteria);
        email = (TextInputEditText) findViewById(R.id.edit_email);
        lv_criteria = (ListView) findViewById(R.id.lv_criteria);
        lv_email = (ListView) findViewById(R.id.lv_email);
        linearLayout = (LinearLayout) findViewById(R.id.list_criteria);
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
//        addCriteriaInFirebase();
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

    public void deleteCriteria(int i) {
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

                listCriteria.remove(i);

                criteriaAdapter.notifyDataSetChanged();
                updateLinearLayoutHeight();
                dialog.dismiss();
            }
        });
        dialog.show();
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

    public void addCriteria() {
        addCriteria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listCriteria.size() <= 2 && !criteria.getText().toString().equals("")) {
                    listCriteria.add(criteria.getText().toString());
                    criteriaAdapter = new CriteriaContestAdapter(getApplicationContext(), DetailContestActivity.this, listCriteria);
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
                    emailAdapter = new EmailContestAdapter(getApplicationContext(), DetailContestActivity.this, listEmail);
                    lv_email.setAdapter(emailAdapter);
                    updateLinearLayoutHeightEmail();
                    email.setText("");
                }
            }
        });
    }

    public void updateLV() {
        listCriteria = new ArrayList<String>();
        criteriaAdapter = new CriteriaContestAdapter(getApplicationContext(), DetailContestActivity.this, listCriteria);
        lv_criteria.setAdapter(criteriaAdapter);
    }

//    public void createCompetition(String idCriteria, String idEmail) {
//        Map<String, Object> competition = new HashMap<>();
//        competition.put("TITLE", title.getText().toString());
//        competition.put("CREATOR", idCreator);
//        competition.put("TIME_START", convertStringToDate(timeStart.getText().toString()));
//        competition.put("TIME_END", convertStringToDate(timeEnd.getText().toString()));
//        competition.put("CONTENT", content.getText().toString());
//        competition.put("CRITERIALIST", idCriteria);
//        competition.put("EMAILLIST", idEmail);
//        // Add a new document with a generated ID
//        db.collection("COMPETITION")
//                .add(competition)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        finish();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                    }
//                });
//    }
//
//    public void addCriteriaInFirebase() {
//        createContest = (Button) findViewById(R.id.createCompetition);
//
//        createContest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (listCriteria.size() > 0 && listEmail.size() > 0 && !title.getText().toString().equals("") && !timeEnd.getText().toString().equals("")
//                        && !timeStart.getText().toString().equals("") && !content.getText().toString().equals("")) {
//                    Map<String, Object> competition = new HashMap<>();
//                    for (int i = 0; i < listCriteria.size(); i++) {
//                        competition.put("CRITERIA" + i, listCriteria.get(i));
//                    }
//
//                    db.collection("CRITERIALIST")
//                            .add(competition)
//                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                @Override
//                                public void onSuccess(DocumentReference documentReference) {
//                                    addEmailInFirebase(documentReference.getId());
//                                }
//                            })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                }
//                            });
//                }
//            }
//        });
//    }
//
//    public void addEmailInFirebase(String idCriteria) {
//        Map<String, Object> competition = new HashMap<>();
//        for (int i = 0; i < listEmail.size(); i++) {
//            competition.put("EMAIL" + i, listEmail.get(i));
//        }
//
//        db.collection("EMAILLIST")
//                .add(competition)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        createCompetition(idCriteria, documentReference.getId());
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                    }
//                });
//    }

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
                        showTimePickerDialogStart(context);
                    }
                },
                year,
                month,
                day
        );

        // Hiển thị hộp thoại chọn ngày tháng năm
        datePickerDialog.show();
    }

    private void showTimePickerDialogStart(Context context) {
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
                        timeStart.setText(timeStart.getText().toString() + " " + selectedTime);
                    }
                },
                hour,
                minute,
                true // true nếu sử dụng định dạng 24 giờ, false nếu sử dụng định dạng 12 giờ
        );

        // Hiển thị hộp thoại chọn giờ
        timePickerDialog.show();
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
                        showTimePickerDialog(context);
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
                        timeEnd.setText(timeEnd.getText().toString() + " " + selectedTime);
                    }
                },
                hour,
                minute,
                true // true nếu sử dụng định dạng 24 giờ, false nếu sử dụng định dạng 12 giờ
        );

        // Hiển thị hộp thoại chọn giờ
        timePickerDialog.show();
    }

    public String convertStringToDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return date.getTime() + "";
    }

    public Date convertTimeToDate(String time) {
        BigInteger number = new BigInteger(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(number.longValue());
        Date date = calendar.getTime();
        return date;
    }

    public String convertDateToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String formattedDate = dateFormat.format(date);
        return formattedDate;
    }

    public void loadContest() {
        DocumentReference documentReference = db.collection("COMPETITION").document(idContest);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // Dữ liệu của collection
                    DocumentSnapshot documentSnapshot = task.getResult();

                    title.setText(documentSnapshot.getString("TITLE"));
                    timeStart.setText(convertDateToString(convertTimeToDate(documentSnapshot.getString("TIME_START"))));
                    timeEnd.setText(convertDateToString(convertTimeToDate(documentSnapshot.getString("TIME_END"))));
                    content.setText(documentSnapshot.getString("CONTENT"));
                    loadCriteria(documentSnapshot.getString("CRITERIALIST"));
                    loadEmail(documentSnapshot.getString("EMAILLIST"));
                } else {

                }
            }
        });
    }

    public void loadCriteria(String id) {
        DocumentReference documentReference = db.collection("CRITERIALIST").document(id);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // Dữ liệu của collection
                    DocumentSnapshot documentSnapshot = task.getResult();

                    int fieldCount = documentSnapshot.getData().size();
                    for (int i = 0; i < fieldCount; i++) {
                        listCriteria.add(documentSnapshot.getString("CRITERIA" + i));
                    }
                    criteriaAdapter = new CriteriaContestAdapter(getApplicationContext(), DetailContestActivity.this, listCriteria);
                    lv_criteria.setAdapter(criteriaAdapter);
                    updateLinearLayoutHeight();
                } else {

                }
            }
        });
    }

    public void loadEmail(String id) {
        DocumentReference documentReference = db.collection("EMAILLIST").document(id);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // Dữ liệu của collection
                    DocumentSnapshot documentSnapshot = task.getResult();

                    int fieldCount = documentSnapshot.getData().size();
                    for (int i = 0; i < fieldCount; i++) {
                        listEmail.add(documentSnapshot.getString("EMAIL" + i));
                    }
                    emailAdapter = new EmailContestAdapter(getApplicationContext(), DetailContestActivity.this, listEmail);
                    lv_email.setAdapter(emailAdapter);
                    updateLinearLayoutHeightEmail();
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
                DocumentReference documentReference = db.collection("COMPETITION").document(idContest);
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (listCriteria.size() > 0 && (listEmail.size() == 2) && !title.getText().toString().equals("") && !timeEnd.getText().toString().equals("")
                                    && !timeStart.getText().toString().equals("") && !content.getText().toString().equals("")) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                deleteCriteriaAndEmail(documentSnapshot.getString("CRITERIALIST"), documentSnapshot.getString("EMAILLIST"));
                                Toast.makeText(getApplicationContext(), "Xóa thành công!", Toast.LENGTH_SHORT).show();
                            }
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
        DocumentReference documentReference = db.collection("COMPETITION").document(idContest);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                } else {

                }
            }
        });
        documentReference = db.collection("EMAILLIST").document(idE);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                } else {

                }
            }
        });
        documentReference = db.collection("CRITERIALIST").document(idC);
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
        DocumentReference documentReference = db.collection("EMAILLIST").document(id);
        Map<String, Object> data = new HashMap<>();
        for (int i = 0; i < listEmail.size(); i++) {
            data.put("EMAIL" + i, listEmail.get(i));
        }
        documentReference.update(data);
    }

    public void updateContest(String idC, String idE) {
        DocumentReference documentReference = db.collection("COMPETITION").document(idContest);
        Map<String, Object> competition = new HashMap<>();
        competition.put("TITLE", title.getText().toString());
        Toast.makeText(getApplicationContext(), title.getText().toString(), Toast.LENGTH_SHORT).show();
        competition.put("CREATOR", idCreator);
        competition.put("TIME_START", convertStringToDate(timeStart.getText().toString()));
        competition.put("TIME_END", convertStringToDate(timeEnd.getText().toString()));
        competition.put("CONTENT", content.getText().toString());
        competition.put("CRITERIALIST", idC);
        competition.put("EMAILLIST", idE);
        documentReference.update(competition);
    }

    public void updateContestFinal() {
        DocumentReference documentReference = db.collection("COMPETITION").document(idContest);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (listCriteria.size() > 0 && (listEmail.size() == 2) && !title.getText().toString().equals("") && !timeEnd.getText().toString().equals("")
                            && !timeStart.getText().toString().equals("") && !content.getText().toString().equals("")) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        updateCriteria(documentSnapshot.getString("CRITERIALIST"));
                        updateEmail(documentSnapshot.getString("EMAILLIST"));
                        updateContest(documentSnapshot.getString("CRITERIALIST"), documentSnapshot.getString("EMAILLIST"));
                        Toast.makeText(getApplicationContext(), "Cập nhật thành công!!", Toast.LENGTH_SHORT).show();
                    }
                } else {

                }
            }
        });
    }
}