package com.example.note.ui.note;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.note.R;
import com.example.note.model.LocalData;
import com.example.note.model.Note;
import com.example.note.model.dataBase.UserDataBase;
import com.example.note.model.dataBase.UserDataBaseHelper;

public class NoteAdapter extends CursorAdapter {
   /* Context ctx;
    LayoutInflater lInflater;
    LocalData ld;
    UserDataBaseHelper db ;*/

    NoteAdapter(Context context, Cursor c) {
        super(context, c);
    }/*(Context context, LocalData ld) {
        ctx = context;
        this.db = ;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }*/
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View retView = inflater.inflate(R.layout.list, parent, false);

            return retView;
        }


        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            /*TextView textViewPersonName = (TextView) view.findViewById(R.id.tv_person_name);
        textViewPersonName.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))));

        TextView textViewPersonPIN = (TextView) view.findViewById(R.id.tv_person_pin);
        textViewPersonPIN.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(2))));*/

            ((TextView) view.findViewById(R.id.noteName)).setText(cursor.getString(1));
            ((TextView) view.findViewById(R.id.noteSubtitle)).setText(cursor.getString(2));
        }
   /* @Override
  *//*  public int getCount() {
        return ld.getmNotes().size();
    }*/

  /*  @Override
    public Note getItem(int position) {
        return ld.getmNotes().get(position);
    }*/

   // @Override
  /*  public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
*/
   // @Override
   /* public long getItemId(int position) {
        return ld.getmNotes().get(position).getId();
    }*/

    /*@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.list, parent, false);
        }

        Note d = getItem(position);

        ((TextView) view.findViewById(R.id.noteName)).setText(d.getTitle());
        ((TextView) view.findViewById(R.id.noteSubtitle)).setText(d.getDescription());

        return view;

    }*/



}
