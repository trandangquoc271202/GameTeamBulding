package vn.edu.hcmuaf.fit.gameteambulding;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CriteriaHolder extends RecyclerView.ViewHolder {
    TextView criterion;
    EditText score;
    public CriteriaHolder(@NonNull View itemView) {
        super(itemView);
        criterion=itemView.findViewById(R.id.criterion);
        score=itemView.findViewById(R.id.crit_score);
        score.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
