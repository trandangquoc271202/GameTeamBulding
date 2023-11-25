package vn.edu.hcmuaf.fit.gameteambulding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CriteriaAdapter extends ArrayAdapter<String> {
    Context context;
    TextView tv_criteria;
    ArrayList<String> list;
    View v_del;
    CreateCompetitionActivity main;
    public CriteriaAdapter(@NonNull Context context, CreateCompetitionActivity activity, ArrayList<String> list) {
        super(context, 0, list);
        this.context = context;
        this.list = list;
        this.main = activity;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.criteria, null);
        }
        final String item = list.get(position);
        if (v != null){
            tv_criteria = v.findViewById(R.id.textView);
//            v_del = v.findViewById(R.id.delete);
            tv_criteria.setText(item);
        }

        return v;
    }
}
