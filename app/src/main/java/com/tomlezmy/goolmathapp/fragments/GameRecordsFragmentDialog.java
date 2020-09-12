package com.tomlezmy.goolmathapp.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tomlezmy.goolmathapp.R;
import com.tomlezmy.goolmathapp.adapters.GameRecordsAdapter;
import com.tomlezmy.goolmathapp.model.GameRecord;

import java.util.List;

/**
 * This fragment all game records of a specific level with a {@link RecyclerView} of {@link GameRecord}
 */
public class GameRecordsFragmentDialog extends DialogFragment {
    List<GameRecord> records;

    public GameRecordsFragmentDialog(List<GameRecord> records) {
        this.records = records;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.fragment_game_records, (ViewGroup) getActivity().findViewById(R.id.root_layout), false);
        builder.setView(dialogView);
        RecyclerView recyclerView = dialogView.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new GameRecordsAdapter(records, getContext()));
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return dialog;
    }
}
