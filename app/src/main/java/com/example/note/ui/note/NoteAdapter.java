package com.example.note.ui.note;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.note.R;
import com.example.note.model.LocalData;
import com.example.note.model.Note;
import com.example.note.model.dataBase.UserDataBase;
import com.example.note.model.dataBase.UserDataBaseHelper;

public class NoteAdapter extends CursorAdapter {

    public OnDeleteItemListner onDeleteItemListner;

    NoteAdapter(Context context, Cursor c) {
        super(context, c);
    }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View retView = inflater.inflate(R.layout.list, parent, false);

            return retView;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            ((TextView) view.findViewById(R.id.noteName)).setText(cursor.getString(1));
            ((TextView) view.findViewById(R.id.noteSubtitle)).setText(cursor.getString(2));
            final long noteID = cursor.getLong(0);
            ((ImageButton) view.findViewById(R.id.imgBtn)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    invokeOnDeleteItemListner(noteID);
                }
            });

        }

    public void setOnDeleteClickListener(OnDeleteItemListner listener) {
        onDeleteItemListner = listener;

    }

    public void invokeOnDeleteItemListner(long id) {
        if (onDeleteItemListner != null) {
            onDeleteItemListner.onItemDeleteClick(id);
        }
    }

    public interface OnDeleteItemListner {
        void onItemDeleteClick(long id);

    }


}
