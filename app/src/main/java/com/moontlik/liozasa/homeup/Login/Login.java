package com.moontlik.liozasa.homeup.Login;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.widget.LoginButton;
import com.moontlik.liozasa.homeup.MainActivity;
import com.moontlik.liozasa.homeup.R;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class Login extends AppCompatActivity {

    boolean isExist = false;
    EditText mail, pass;
    Button login, signUp;
    LoginButton loginWithFacebook;
    List<String> permissions = Arrays.asList("public_profile","email"); //Permissions of user that connect with facebook, ("public_profile", "email", "user_friends").
    //String firstName, lastName, email, id, gender, friends, events, link;
    ParseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initLayout();
        initEvents();
    }

    private void initLayout() {
        mail = (EditText) findViewById(R.id.etEmail);
        pass = (EditText) findViewById(R.id.etPassword);
        login = (Button) findViewById(R.id.btnLogin);
        signUp = (Button) findViewById(R.id.btnSignUp);
        loginWithFacebook = (LoginButton) findViewById(R.id.btnFacebook);
    }

    /**
     * The function start when that user connect to application in first time
     * The function create the user and save the data in parse.
     */
    private synchronized void initEvents() {
        //Start if user wont login.
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkOn(getBaseContext())) {
                    Toast.makeText(getBaseContext(), "No network connection", Toast.LENGTH_SHORT).show();
                }

                if (!isDataValid()) {
                    Toast.makeText(getBaseContext(), "oops... email or password is incorrect", Toast.LENGTH_SHORT).show();
                } else {
                    login(getEmail(), getPassword());
                }
            }
        });

        //Start if user not logged, and he wont to register.
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, SignUp.class));
            }
        });

        //Start if user connect with facebook.
        loginWithFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseFacebookUtils.logInWithReadPermissionsInBackground(Login.this, permissions, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user == null) {
                            Toast.makeText(getApplicationContext(), "The user cancelled the Facebook login.", Toast.LENGTH_SHORT).show();
                        } else if (user.isNew()) {
                            Toast.makeText(getApplicationContext(), "User signed up and logged in through Facebook.", Toast.LENGTH_SHORT).show();
                            saveUser();
                        } else {
                            currentUser = ParseUser.getCurrentUser();
                            if (ifUserExistInApp(currentUser.getString("my_id"))) {
                                Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Login.this, MainActivity.class)); //Connecting and move to mainActivity.
                            } else {
                                saveUser();
                                Toast.makeText(getApplicationContext(), "User logged in through Facebook!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Login.this, MainActivity.class)); //Connecting and move to mainActivity.
                            }
                        }
                    }
                });
            }
        });
    }

    /**
     * Save information about user in parse server.
     */
    private void saveUser() {
        currentUser = ParseUser.getCurrentUser();
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        if (object != null) {
                            currentUser.put("my_id", object.optString("id"));
                            currentUser.put("first_name", object.optString("first_name"));
                            currentUser.put("last_name", object.optString("last_name"));
                            currentUser.put("email", object.optString("email"));
                            currentUser.put("gender", object.optString("gender"));
                            currentUser.put("link", object.optString("link"));
                            currentUser.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Log.d("Moontlik", "Saved!");
                                        startActivity(new Intent(Login.this, MainActivity.class)); //Connecting and move to mainActivity.
                                    } else {
                                        Log.d("Moontlik", "Don't saved!");
                                    }
                                }
                            });
                        } else {
                            Log.d("object", "object null");
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,link,email,gender");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * The function test login to system, if user exist return user, else return null.
     * @param email
     * @param password
     * @return
     */
    public void login(String email, String password) {
        ParseUser.logInInBackground(email, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    Toast.makeText(getApplicationContext(), "connecting...", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login.this, MainActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "oops... User not exist.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Checking if network connect.
     * @param context
     * @return
     */
    public boolean isNetworkOn(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Checking if email and password valid.
     * @return
     */
    public boolean isDataValid() {
        boolean isEmailValid = Patterns.EMAIL_ADDRESS.matcher(getEmail()).matches();
        boolean isPasswordValid = !getPassword().isEmpty();
        return isEmailValid && isPasswordValid;
    }


    public String getEmail() {
        return mail.getText().toString().trim();
    }

    public String getPassword() {
        return pass.getText().toString().trim();
    }

    /**
     * This function check if this user exist in app.
     * @param user_id
     * @return
     */
    private boolean ifUserExistInApp(String user_id){
        isExist = false;
        ParseQuery<ParseObject> query_of_user = ParseQuery.getQuery("User");
        query_of_user.whereMatches("my_id", user_id);
        query_of_user.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject user, ParseException e) {
                if (user != null) {
                    //Check if user exist.
                    isExist = true;
                }
            }
        });
        return isExist;
    }

    /**
     * Close the Application if user push on BackPressed button
     */
    public void onBackPressed() {
        finish();
        System.exit(0);
    }
}
