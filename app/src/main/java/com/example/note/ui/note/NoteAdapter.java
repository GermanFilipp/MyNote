package com.example.note.ui.note;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.note.R;
import com.example.note.model.LocalData;
import com.example.note.model.Note;

public class NoteAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    LocalData ld;

    NoteAdapter(Context context, LocalData ld) {
        ctx = context;
        this.ld = ld;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return ld.getmNotes().size();
    }

    @Override
    public Note getItem(int position) {
        return ld.getmNotes().get(position);
    }

    @Override
    public long getItemId(int position) {
        return ld.getmNotes().get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.list, parent, false);
        }

        Note d = getItem(position);

        ((TextView) view.findViewById(R.id.noteName)).setText(d.getTitle());
        ((TextView) view.findViewById(R.id.noteSubtitle)).setText(d.getDescription());

        return view;

    }

}
