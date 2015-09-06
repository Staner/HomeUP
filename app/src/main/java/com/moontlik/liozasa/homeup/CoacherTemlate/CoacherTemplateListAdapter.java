package com.moontlik.liozasa.homeup.CoacherTemlate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.moontlik.liozasa.homeup.MainActivity;
import com.moontlik.liozasa.homeup.R;

import java.util.List;

/**
 * Created by Sergei on 9/6/2015.
 */
public class CoacherTemplateListAdapter extends BaseAdapter {





    TextView tvSubject;
    Context context;
    LayoutInflater inflater;

    public CoacherTemplateListAdapter(List<CoacherTemplate> COACHER_TEMPLATES, Context context){
        super();
        MainActivity.COACHER_TEMPLATS = COACHER_TEMPLATES;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return MainActivity.COACHER_TEMPLATS.size();
    }

    @Override
    public Object getItem(int position) {
        return MainActivity.COACHER_TEMPLATS.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.list_view_coacher_template, parent, false);

        tvSubject = (TextView) convertView.findViewById(R.id.tvSubject);

        tvSubject.setText(MainActivity.COACHER_TEMPLATS.get(position).getSubject());


        return (convertView);
    }
}
