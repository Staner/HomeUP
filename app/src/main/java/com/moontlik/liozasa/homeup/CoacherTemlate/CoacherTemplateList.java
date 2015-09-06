package com.moontlik.liozasa.homeup.CoacherTemlate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.moontlik.liozasa.homeup.MainActivity;
import com.moontlik.liozasa.homeup.R;

public class CoacherTemplateList extends AppCompatActivity {

    ListView coacherTemplateListView;
    CoacherTemplateListAdapter coacherTemplateListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coacher_template_list);



        coacherTemplateListView = (ListView) findViewById(R.id.listViewCoacherTemplate);
        coacherTemplateListAdapter = new CoacherTemplateListAdapter(MainActivity.COACHER_TEMPLATS,this);
        coacherTemplateListView.setAdapter(coacherTemplateListAdapter);

    }
}
