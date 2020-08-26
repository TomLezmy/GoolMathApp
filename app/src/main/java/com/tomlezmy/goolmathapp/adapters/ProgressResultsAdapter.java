package com.tomlezmy.goolmathapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tomlezmy.goolmathapp.R;
import com.tomlezmy.goolmathapp.game.ProgressResult;

import java.util.List;

/**
 * An adapter for {@link com.tomlezmy.goolmathapp.fragments.ProgressFragment} to display all progress data in {@link RecyclerView}
 */
public class ProgressResultsAdapter extends RecyclerView.Adapter<ProgressResultsAdapter.ProgressResultsViewHolder> {

    private List<ProgressResult> progressResults;
    private Context context;


    /**
     * Class constructor
     * @param progressResults A list of progress data to display in recycler
     * @param context Current context
     */
    public ProgressResultsAdapter(List<ProgressResult> progressResults, Context context) {
        this.progressResults = progressResults;
        this.context = context;
    }

    /**
     * A {@link androidx.recyclerview.widget.RecyclerView.ViewHolder} to access items in {@link RecyclerView
     */
    public class ProgressResultsViewHolder extends RecyclerView.ViewHolder {
        TextView levelTv, timesPlayTv, highScoreTv, finishedTv;

        public ProgressResultsViewHolder(View itemView) {
            super(itemView);
            finishedTv = itemView.findViewById(R.id.tv_finished_level);
            levelTv = itemView.findViewById(R.id.tv_level);
            timesPlayTv = itemView.findViewById(R.id.tv_times_played);
            highScoreTv = itemView.findViewById(R.id.tv_highScore);
        }
    }

    /**
     * Inflates the recycler item view and instantiates {@link ProgressResultsViewHolder}
     */
    @NonNull
    @Override
    public ProgressResultsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_results_cell, parent,false);
        return new ProgressResultsViewHolder(view);
    }

    /**
     * Fills the {@link ProgressResultsViewHolder} with the current item data
     */
    @Override
    public void onBindViewHolder(@NonNull ProgressResultsViewHolder holder, int position) {
        ProgressResult progressResult = progressResults.get(position);

        String level =  progressResult.getLevel();
        String tv_level_text = String.format(context.getResources().getString(R.string.in_the_level_x), level);
        holder.levelTv.setText(tv_level_text);

        int timesPlayed =  progressResult.getTimesPlayed();
        String tv_timesPlayed_text = String.format(context.getResources().getString(R.string.you_played_x_times), timesPlayed);
        holder.timesPlayTv.setText(tv_timesPlayed_text);

        int highScore =  progressResult.getHighScore();
        String tv_highScore_text = String.format(context.getResources().getString(R.string.your_high_score_is_x), highScore);
        holder.highScoreTv.setText(tv_highScore_text);
        if (timesPlayed == 0) {
            ((CardView)(holder.itemView)).setCardBackgroundColor(ContextCompat.getColor(context, R.color.disabled_card));
        }
        if (progressResult.isFinished()) {
            holder.finishedTv.setVisibility(View.VISIBLE);
        }
        // If user made several mistakes in a specific question type then display a "need to practice more" message instead of "level finished"
        for (int weight : progressResult.getWeightList()) {
            if (weight > 2) {
                holder.finishedTv.setVisibility(View.VISIBLE);
                holder.finishedTv.setText(R.string.need_more_practice);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return progressResults.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
