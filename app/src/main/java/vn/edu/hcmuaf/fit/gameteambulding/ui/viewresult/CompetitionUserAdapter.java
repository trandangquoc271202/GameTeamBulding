package vn.edu.hcmuaf.fit.gameteambulding.ui.viewresult;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import vn.edu.hcmuaf.fit.gameteambulding.Model.Competition;
import vn.edu.hcmuaf.fit.gameteambulding.Model.CompetitionUser;
import vn.edu.hcmuaf.fit.gameteambulding.R;

import java.util.List;

public class CompetitionUserAdapter extends RecyclerView.Adapter<CompetitionUserAdapter.ViewHolder> {
    private Competition competition;


    public CompetitionUserAdapter(Competition competition) {
        this.competition = competition;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_result_exam, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind data to your TextViews and ImageView here using competition.getUser1() and competition.getUser2()
//        holder.textViewName1.setText(competition.getUser1().getName());
        // Set other TextViews and ImageView accordingly
        holder.bind(competition);
    }

    @Override
    public int getItemCount() {
        // Return the number of items in the adapter
        return 1; // Assuming you only have one competition. Adjust accordingly if you have a list of competitions.
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView timeStart, timeEnd, name1, name2, totalPoint1, totalPoint2, textViewResult1, textViewResult2;
        ImageView img1, img2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            timeStart = itemView.findViewById(R.id.textViewStart);
            timeEnd = itemView.findViewById(R.id.textTimeEnd);
            name1 = itemView.findViewById(R.id.textViewName1);
            name2 = itemView.findViewById(R.id.textViewName2);
            totalPoint1 = itemView.findViewById(R.id.textViewTotalPoint1);
            totalPoint2 = itemView.findViewById(R.id.textViewTotalPoint2);
            img1 = itemView.findViewById(R.id.imageView1);
            img2 = itemView.findViewById(R.id.imageView2);
            textViewResult1 = itemView.findViewById(R.id.textViewResult1);
            textViewResult2 = itemView.findViewById(R.id.textViewResult2);
        }

        public void bind(Competition competition) {
            // Bind data to your TextViews and ImageView here using competition.getUser1() and competition.getUser2()
//            textViewName1.setText(competition.getUser1().getName());
            // Set other TextViews and ImageView accordingly
            timeStart.setText(competition.getStartAt());
            timeEnd.setText(competition.getEndAt());
//            name1.setText(competition.getUser1().getName());
//            name2.setText(competition.getUser2().getName());
            totalPoint1.setText(competition.getUser1().getTotalScore() + "");
            totalPoint2.setText(competition.getUser2().getTotalScore() + "");
            textViewResult1.setText(competition.getUser1().getResult());
            textViewResult2.setText(competition.getUser2().getResult());
            img1.setImageResource(competition.getUser1().getResult() != "Thua" ? R.drawable.win : R.drawable.lose);
            img2.setImageResource(competition.getUser2().getResult() != "Thua" ? R.drawable.win : R.drawable.lose);
        }
    }
}



/*
 */
