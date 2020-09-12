package com.tomlezmy.goolmathapp.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tomlezmy.goolmathapp.R;
import com.tomlezmy.goolmathapp.model.GameRecord;

import java.util.List;

/**
 * An adapter for {@link com.tomlezmy.goolmathapp.fragments.GameRecordsFragmentDialog} to display all game records for a specific level in a {@link RecyclerView}
 */
public class GameRecordsAdapter extends RecyclerView.Adapter<GameRecordsAdapter.GameRecordsViewHolder> {
    List<GameRecord> gameRecords;
    Context context;

    /**
     * Class constructor
     * @param gameRecords A list of game record data to display in recycler
     * @param context Current context
     */
    public GameRecordsAdapter(List<GameRecord> gameRecords, Context context) {
        this.gameRecords = gameRecords;
        this.context = context;
    }

    /**
     * A {@link androidx.recyclerview.widget.RecyclerView.ViewHolder} to access items in {@link RecyclerView}
     */
    public class GameRecordsViewHolder extends RecyclerView.ViewHolder {
        TextView timeStampTv, scoreTv;

        public GameRecordsViewHolder(View itemView) {
            super(itemView);
            timeStampTv = itemView.findViewById(R.id.tv_timestamp);
            scoreTv = itemView.findViewById(R.id.tv_score);
        }
    }

    /**
     * Inflates the recycler item view and instantiates {@link GameRecordsAdapter.GameRecordsViewHolder}
     */
    @NonNull
    @Override
    public GameRecordsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_record_cell, parent,false);
        return new GameRecordsViewHolder(view);
    }

    /**
     * Fills the {@link GameRecordsAdapter.GameRecordsViewHolder} with the current item data
     */
    @Override
    public void onBindViewHolder(@NonNull GameRecordsViewHolder holder, int position) {
        GameRecord gameRecord = gameRecords.get(position);
        holder.timeStampTv.setText(DateFormat.format("dd-MM-yyyy HH:mm:ss", gameRecord.getTimeStamp()));
        String tv_highScore_text = String.format(context.getResources().getString(R.string.score_is_x), gameRecord.getCorrectAnswers());
        holder.scoreTv.setText(tv_highScore_text);
    }

    @Override
    public int getItemCount() {
        return gameRecords.size();
    }
}
