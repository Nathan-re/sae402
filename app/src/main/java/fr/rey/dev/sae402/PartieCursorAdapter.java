package fr.rey.dev.sae402;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import fr.rey.dev.sae402.R;

public class PartieCursorAdapter extends SimpleCursorAdapter {
    public PartieCursorAdapter(Context context, Cursor cursor) {
        super(context, R.layout.item_partie, cursor, new String[]{}, new int[]{}, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_partie, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textViewEquipes = view.findViewById(R.id.textView_equipes);
        TextView textViewScore = view.findViewById(R.id.textView_score);
        TextView textViewModeJeu = view.findViewById(R.id.textView_modeJeu);
        TextView textViewId = view.findViewById(R.id.textView_id);

        String equipes = cursor.getString(cursor.getColumnIndexOrThrow("equipes"));
        int score = cursor.getInt(cursor.getColumnIndexOrThrow("score"));
        String modeJeu = cursor.getString(cursor.getColumnIndexOrThrow("typePartie"));
        int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));

        textViewEquipes.setText(equipes);
        textViewScore.setText(String.valueOf(score));
        textViewModeJeu.setText(modeJeu);
        textViewId.setText(String.valueOf(id));
    }
}

