package com.moontlik.liozasa.homeup;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.moontlik.liozasa.homeup.CoacherTemlate.CoacherTemplate;
import com.moontlik.liozasa.homeup.Login.Login;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    //Logic param
    ParseUser currentUser;
    public static int counterLoadFromParse = 0;
    public static List<CoacherTemplate> COACHER_TEMPLATS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = ParseUser.getCurrentUser();
        if(currentUser == null) {
            Log.d("Moontlik", "currentUser null");
            startActivity(new Intent(this, Login.class));
        }else {
            setContentView(R.layout.activity_main_appbar);
            initLayout();



        }
         // load constant from parse
        if (counterLoadFromParse == 0) {
            counterLoadFromParse++;
            loadFromParse();

        }
    }



    private void initLayout() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ViewPager viewPager = (ViewPager)findViewById(R.id.viewpager);
        TabLayout tabs = (TabLayout)findViewById(R.id.tabs);
        setupViewPager(viewPager);
        tabs.setupWithViewPager(viewPager);
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer,(DrawerLayout)findViewById(R.id.drawer_layout), toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), "Home");
        adapter.addFragment(new MissionFragment(), "Mission");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.navigate){
            startActivity(new Intent(this, SubActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }



    private void loadFromParse() {

        loadCoacherTemplate();



    }

    private void loadCoacherTemplate() {

        COACHER_TEMPLATS = new ArrayList<>();

        final ParseQuery<ParseObject> coacherTemplate = new ParseQuery<ParseObject>("Coacher_Template");

        coacherTemplate.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null && parseObjects != null) {
                    for (ParseObject parseObject : parseObjects) {


                            String subject = parseObject.getString("subject");
                            String group = parseObject.getString("group");
                            String description = parseObject.getString("description");
                            String repeat = parseObject.getString("repeat");


                         CoacherTemplate coacherTemplate = new CoacherTemplate(subject,group,description,repeat);
                         COACHER_TEMPLATS.add(coacherTemplate);

                        }
                    }


                }



        });

    }


}
