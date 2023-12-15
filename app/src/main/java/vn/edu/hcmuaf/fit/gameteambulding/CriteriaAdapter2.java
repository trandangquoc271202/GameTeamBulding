package vn.edu.hcmuaf.fit.gameteambulding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CriteriaAdapter2 extends RecyclerView.Adapter<CriteriaHolder> {
    Context context;
    List<CriterionItem> items;

    public CriteriaAdapter2( Context context ,List<CriterionItem> items) {
        this.context=context;
        this.items = items;
    }

    @NonNull
    @Override
    public CriteriaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new CriteriaHolder(LayoutInflater.from(context).inflate(R.layout.criterion_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CriteriaHolder holder, int position) {
        holder.criterion.setText(items.get(position).getCriterion());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
