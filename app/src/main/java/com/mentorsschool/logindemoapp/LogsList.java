package com.mentorsschool.logindemoapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class LogsList extends ArrayAdapter<Log> {
    private Activity context;
    List<Log> logs;

    public LogsList(Activity context, List<Log> logs) {
        super(context, R.layout.layout_logs, logs);
        this.context = context;
        this.logs = logs;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_logs, null, true);

        TextView textLog = listViewItem.findViewById(R.id.textLog);
        TextView textDate = listViewItem.findViewById(R.id.textDate);

        Log log = logs.get(position);
        textLog.setText(log.level);
        textDate.setText("NULL DATE");

        return listViewItem;
    }
}