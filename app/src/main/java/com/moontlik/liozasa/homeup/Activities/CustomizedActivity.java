package com.moontlik.liozasa.homeup.Activities;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.moontlik.liozasa.homeup.Constants;
import com.moontlik.liozasa.homeup.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateChangedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import me.everything.providers.android.calendar.Calendar;
import me.everything.providers.android.calendar.CalendarProvider;

import static com.moontlik.liozasa.homeup.R.array.group_from_event;
import static com.moontlik.liozasa.homeup.R.array.repeat_array;


public class CustomizedActivity extends AppCompatActivity {

    //the date we set from the Dialog
    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();

    //Calendar Account Name
    private static final String MY_ACCOUNT_NAME = "Home Up" ;
    //SharedPref save/read
    private SharedPreferences pref;
    //boolean for first time load
    private boolean isCalendarHasBeenCreated;
    //Calendar View
    private MaterialCalendarView calendar;
    //Buttons
    private Button btnLoadCal,btnGroup, btnRepeat,btnSendData;
    //Arraylist to images from group choice
    private ArrayList<Integer> groupImages;
    //TextViews
    private TextView tvGroupSelected,tvRepeat;
    private static TextView tvCalendarDate;
    //Calendar data to send
    static int year, month, day;
    public AlertDialog dialog;
    //EditText
    private EditText etSubject,etDescription;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customized);
        // We set pref to Constant file we've create on device -> reference
        pref = getSharedPreferences(Constants.MEMORY_FILE_ON_DEVICE, Context.MODE_PRIVATE);
        //Layout
        initLayout();
        //events
        initEvents();

        //Check if the user created a calendar, if not we going to create one
        checkIfWeCreatedCalendar();
    }

    private void initLayout() {
        //TextViews
        tvGroupSelected = (TextView) findViewById(R.id.tvGroupSelected);
        tvCalendarDate = (TextView) findViewById(R.id.tvCalendarDate);
        tvRepeat = (TextView) findViewById(R.id.tvRepeat);
        //Buttons
        btnSendData = (Button) findViewById(R.id.btnSendData);
        btnLoadCal = (Button) findViewById(R.id.btnLoadCal);
        btnGroup = (Button) findViewById(R.id.btnGroup);
        btnRepeat = (Button) findViewById(R.id.btnRepeat);
        //ArrayList
        groupImages = new ArrayList<Integer>();
        groupImages.add(R.mipmap.ic_launcher);
        groupImages.add(R.mipmap.ic_launcher);
        groupImages.add(R.mipmap.ic_launcher);
        //EditText
        etSubject = (EditText) findViewById(R.id.etSubject);
        etDescription = (EditText) findViewById(R.id.etDescription);

    }

    private void initEvents() {
        //We load the calendar into dialog
        btnLoadCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleCalendarDialogFragment().show(getSupportFragmentManager(), "test-simple-calendar");

            }
        });
        // choose from list
        btnGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CustomizedActivity.this);
                builder.setTitle("group choose");
                builder.setItems(group_from_event, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String[] fromResources = getResources().getStringArray(R.array.group_from_event);
                        tvGroupSelected.setText(fromResources[which]);
                    }
                });
                builder.setIcon(R.mipmap.ic_launcher);
                dialog = builder.create();
                dialog.show();
            }
        });

        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CustomizedActivity.this);
                builder.setTitle("בחר חזרה");
                builder.setItems(repeat_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String[] fromResources = getResources().getStringArray(R.array.repeat_array);
                        tvRepeat.setText(fromResources[which]);
                    }
                });
                builder.setIcon(R.mipmap.ic_launcher);
                dialog = builder.create();
                dialog.show();
            }
        });

        btnSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //we get the text from the tvCalendarDate
                String textViewStringToDate = (String) tvCalendarDate.getText();
                Log.d("textViewStringToDAte", textViewStringToDate);
                //Split it to ints
                String[] parts = textViewStringToDate.split("-");
                int year = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                int day = Integer.parseInt(parts[2]);
                // we have a problem with month, it's always adding 1 month a head
                month -= 1;
                Log.d("yearMonthDay", String.valueOf(year + "-" + month + "-" + day));

                //TODO: I NEED TO DO IF ELSE ( if I finish to submit all the fields, then let me send the data, else show an alert)
                createAnEventOnCalendar(getCalendarID(), etSubject.getText().toString(), tvGroupSelected.getText().toString() + ": " + etDescription.getText().toString(), year, month, day);
            }
        });

    }

    /**
     * Get the id of Calendar
     */
    private long getCalendarID(){
        String idOfCalendar = "Home Up";



        CalendarProvider provider = new CalendarProvider(this);
        List<Calendar> calendars = provider.getCalendars().getList();
        //let's run on calendars
        for (Calendar cal : calendars) {
            Log.d("allCalNames", cal.accountName);
            if (cal.accountName.compareTo(idOfCalendar) == 0 ){
                Log.d("theIdIs", String.valueOf(cal.id));

                return cal.id;
            }
        }
        return 0;
    }
    /*
    With this method we create the event on Calendar
    TODO: add Repeat on event , RRULE & XRULE FEQ:WEEKLY etc.
     */
    public  void createAnEventOnCalendar(final long theIdOfCalendar, final String title, final String desc, final int year, final int month, final int day){
        // must be off the UI thread
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                long calID = theIdOfCalendar;
                long startMillis = 0;
                long endMillis = 0;
                java.util.Calendar beginTime = java.util.Calendar.getInstance();
                beginTime.set(year, month, day);
                startMillis = beginTime.getTimeInMillis();
                java.util.Calendar endTime = java.util.Calendar.getInstance();
                endTime.set(year, month, day);
                endMillis = endTime.getTimeInMillis();

                ContentResolver cr = getContentResolver();
                ContentValues values = new ContentValues();
                values.put(CalendarContract.Events.DTSTART, startMillis);
                values.put(CalendarContract.Events.DTEND, endMillis);
                values.put(CalendarContract.Events.ALL_DAY,true);
                values.put(CalendarContract.Events.TITLE, title);
                values.put(CalendarContract.Events.DESCRIPTION, desc);
                values.put(CalendarContract.Events.CALENDAR_ID, calID);
                values.put(CalendarContract.Events.EVENT_TIMEZONE, "America/Los_Angeles");
                Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

// get the event ID that is the last element in the Uri
                long eventID = Long.parseLong(uri.getLastPathSegment());
//
// ... do something with event ID
//
//
            }
        });
        thread.start();
    }



    /**
     * Calendar settings
     * Checking if Calendar exists
     */
    private void checkIfWeCreatedCalendar(){
        //let's check if we created an calendar already
        isCalendarHasBeenCreated = pref.getBoolean(Constants.IS_CALENDAR_HAS_BEEN_CREATED, false);
        if (isCalendarHasBeenCreated == false){
            //we didn't create a calendar, let's create one
            //But before we create one need to ask user permission
            askForPermissionDialog();

        }
    }
    //Asking for Permission
    private void askForPermissionDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Permission Issue")
                .setMessage("to create An event you must agree to read/write calendar permission ")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //User Agreed, now we can fire the calendar
                          createNewLocalCalendar();
                    }
                })


                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // User Disagree, we going to change the xml to block
                        setContentView(R.layout.activity_customized_no_permission);

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    //Create a Calendar
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private  void createNewLocalCalendar(){
        ContentValues values = new ContentValues();
        //Name of the calendar -> i've created a Constant
        values.put(
                CalendarContract.Calendars.ACCOUNT_NAME,
                MY_ACCOUNT_NAME);
        //We must keep it local calendar
        values.put(
                CalendarContract.Calendars.ACCOUNT_TYPE,
                CalendarContract.ACCOUNT_TYPE_LOCAL);

        values.put(
                CalendarContract.Calendars.NAME,
                "Home Up");
        values.put(
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                "Home Up");
        //Color of the calendar // TODO: change the color
        values.put(
                CalendarContract.Calendars.CALENDAR_COLOR,
                0xffff0000);

        values.put(
                CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL,
                CalendarContract.Calendars.CAL_ACCESS_OWNER);
        /* Values shows under the calendar, I don't think will use it
        // i leave it for future reference
        values.put(
                CalendarContract.Calendars.OWNER_ACCOUNT,
                "some.account@googlemail.com");
                */
        //Timezone, even tho we probably going to use the default
        values.put(
                CalendarContract.Calendars.CALENDAR_TIME_ZONE,
                "Asia/Jerusalem");
        //we want to sync this calendar and store events
        values.put(
                CalendarContract.Calendars.SYNC_EVENTS,
                1);
        //Buidling Calender with URI builder
        Uri.Builder builder =
                CalendarContract.Calendars.CONTENT_URI.buildUpon();
        builder.appendQueryParameter(
                CalendarContract.Calendars.ACCOUNT_NAME,
                "Home Up");
        builder.appendQueryParameter(
                CalendarContract.Calendars.ACCOUNT_TYPE,
                CalendarContract.ACCOUNT_TYPE_LOCAL);
        builder.appendQueryParameter(
                CalendarContract.CALLER_IS_SYNCADAPTER,
                "true");
        Uri uri =
                getContentResolver().insert(builder.build(), values);
        // we set
        pref = getSharedPreferences(Constants.MEMORY_FILE_ON_DEVICE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(Constants.IS_CALENDAR_HAS_BEEN_CREATED, true);
        editor.commit();

    }

    public static class SimpleCalendarDialogFragment extends DialogFragment implements OnDateChangedListener {

        private TextView textView;
        private Button btnCloseCal;
        private String formateDate;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.dialog_basic, container, false);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            textView = (TextView) view.findViewById(R.id.textView);
            btnCloseCal = (Button) view.findViewById(R.id.btnCloseCal);
            btnCloseCal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    getDialog().dismiss();
                }
            });


            MaterialCalendarView widget = (MaterialCalendarView) view.findViewById(R.id.calendarView);

            widget.setOnDateChangedListener(this);
        }

        @Override
        public void onDateChanged(@NonNull MaterialCalendarView widget, CalendarDay date) {
            textView.setText(FORMATTER.format(date.getDate()));
            formateDate = new SimpleDateFormat("yyyy-MM-dd").format(date.getDate());
            Log.d("checkFormat", formateDate);
            tvCalendarDate.setText(formateDate);

        }

    }
}
