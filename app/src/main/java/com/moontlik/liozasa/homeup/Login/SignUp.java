package com.moontlik.liozasa.homeup.Login;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.moontlik.liozasa.homeup.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUp extends AppCompatActivity {

    EditText email, password, first_name, last_name, mobile;
    Button register, cancel;
    RadioButton male, female;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initLayout();
        initEvent();
    }

    private void initLayout() {
        email = (EditText) findViewById(R.id.etEmail);
        password = (EditText) findViewById(R.id.etPassword);
        first_name = (EditText) findViewById(R.id.etFirstName);
        last_name = (EditText) findViewById(R.id.etLastName);
        mobile = (EditText) findViewById(R.id.etMobile);
        male = (RadioButton) findViewById(R.id.rbMale);
        female = (RadioButton) findViewById(R.id.rbFemale);
        register = (Button) findViewById(R.id.btnRegister);
        cancel = (Button) findViewById(R.id.btnCancel);
    }

    private void initEvent() {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkOn(getBaseContext())) {
                    Toast.makeText(getBaseContext(), "No network connection", Toast.LENGTH_SHORT).show();
                }

                if (!isDataValid()) {
                    Toast.makeText(getBaseContext(), "oops... email or password is incorrect", Toast.LENGTH_SHORT).show();
                } else {
                    signUp(getEmail(), getPassword(), getFirstName(), getLastName(), getMobile());
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this, Login.class));
            }
        });
    }

    /**
     * Checking if network connect.
     * @param context
     * @return
     */
    public boolean isNetworkOn(@NonNull Context context){
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Checking if email and password valid.
     * @return
     */
    public boolean isDataValid(){
        boolean isEmailValid = Patterns.EMAIL_ADDRESS.matcher(getEmail()).matches();
        boolean isPasswordValid = !getPassword().isEmpty();
        return isEmailValid && isPasswordValid;
    }

    public void signUp(String email, String password, String first_name, String last_name, String mobile) {
        ParseUser user = new ParseUser();
        user.setUsername(email);
        user.setPassword(password);
        user.setEmail(email);
        user.put("mobile", mobile);
        user.put("first_name", first_name);
        user.put("last_name", last_name);
        user.put("gender", getGender());
        user.put("my_id", "");
        //user.put("friends", new ArrayList<Friend>());
        //user.put("events", new ArrayList<Event>());
        //user.put("num_of_markers", 0);
        //user.put("num_of_events", 0);
        //user.put("num_of_friends", 0);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(), "good...user create.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUp.this, Login.class));
                }else {
                    Toast.makeText(getApplicationContext(), "oops... User exist.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public String getEmail(){
        return email.getText().toString().trim();
    }

    public String getPassword(){
        return password.getText().toString().trim();
    }

    public String getFirstName(){
        return first_name.getText().toString().trim();
    }

    public String getLastName(){
        return last_name.getText().toString().trim();
    }

    public String getMobile(){
        return mobile.getText().toString().trim();
    }

    public String getGender(){
        if(male.isChecked()){
            return "male";
        }
        return "female";
    }

    public void onBackPressed() {
        finish();
        System.exit(0);
    }
}
