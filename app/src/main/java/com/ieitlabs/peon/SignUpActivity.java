package com.ieitlabs.peon;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.loopj.android.http.HttpGet;

import org.json.JSONObject;

import java.net.URLEncoder;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.util.EntityUtils;
import cz.msebera.android.httpclient.util.TextUtils;

public class SignUpActivity extends AppCompatActivity {
    private UserRegistration mAuthTask = null;
    private View mProgressView;
    private View mLoginFormView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Button mLoginButton = (Button)findViewById(R.id.btn_link_login);
        Button mSignupButton = (Button)findViewById(R.id.btn_signup);
        boolean cancel = false;
        View focusView = null;

        final EditText mETOrgTitle = (EditText)findViewById(R.id.signup_input_name);
        final EditText mETEmail = (EditText)findViewById(R.id.signup_input_email);
        final EditText mPassword = (EditText)findViewById(R.id.signup_input_password);
        final EditText mETDesc = (EditText)findViewById(R.id.signup_input_desc);
       final  RadioButton r1 = (RadioButton) findViewById(R.id.company_radio_btn);



        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent i = new Intent(SignUpActivity.this,LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String OrgType=r1.isChecked()?"Company":"Institute";
                mETOrgTitle.setError(null);
                mETEmail.setError(null);
                mPassword.setError(null);
                mETDesc.setError(null);
                boolean cancel = false;
                if(TextUtils.isEmpty(mETOrgTitle.getText().toString()))
                {
                    mETOrgTitle.setError("Title can't be empty");
                    mETOrgTitle.requestFocus();
                    cancel = true;
                }
                if(!isEmailValid(mETEmail.getText().toString()))
                {
                    mETEmail.setError("Invalid email");
                    mETEmail.requestFocus();
                    cancel = true;
                }
                if(TextUtils.isEmpty(mPassword.getText().toString()))
                {
                    mPassword.setError("Minimum 4 characters");
                    mPassword.requestFocus();
                    cancel = true;
                }
                if(TextUtils.isEmpty(mETDesc.getText().toString()))
                {
                    mETDesc.setError("Description can't be empty");
                    mETDesc.requestFocus();
                    cancel = true;
                }

                if(!cancel)
                {
                    showProgress(true);
                    mAuthTask = new UserRegistration(mETEmail.getText().toString(), mPassword.getText().toString(),mETOrgTitle.getText().toString(),OrgType,mETDesc.getText().toString(),SignUpActivity.this);
                    mAuthTask.execute((Void)null);
                }

            }
        });

        mLoginFormView = findViewById(R.id.signup_form);
        mProgressView = findViewById(R.id.login_progress);
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        if(TextUtils.isEmpty(email))
        {
            return false;
        }
        return email.contains("@") ;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private class UserRegistration extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private final String MOrgTitle;
        private final String MOrgType;
        private final String MOrgDesc;

        private Context context;

        public UserRegistration(String mEmail, String mPassword, String MOrgTitle, String MOrgType, String MOrgDesc, Context context) {
            this.mEmail = mEmail;
            this.mPassword = mPassword;
            this.MOrgTitle = MOrgTitle;
            this.MOrgType = MOrgType;
            this.MOrgDesc = MOrgDesc;
            this.context = context;
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }



            try {
                String url="http://peon.ml/api/orgrequest?title=" + URLEncoder.encode(MOrgTitle,"UTF-8") + "&type=" +  URLEncoder.encode(MOrgType,"UTF-8")  + "&email=" + URLEncoder.encode(mEmail,"UTF-8") + "&pass=" + URLEncoder.encode(mPassword,"UTF-8")  + "&desc=" + URLEncoder.encode(MOrgDesc,"UTF-8");

                HttpClient client = HttpClientBuilder.create().build();
                HttpGet request = new HttpGet(url);

                HttpResponse response = client.execute(request);
                HttpEntity entity = response.getEntity();
                String content = EntityUtils.toString(entity);
                final JSONObject rowObject = new JSONObject(content);
                String res = rowObject.getString("response");
                if(res.equals("success"))
                {
                    String sucCode = rowObject.getString("sucCode");
                    Bundle bundle = new Bundle();
                    //Add your data from getFactualResults method to bundle
                    bundle.putString("toast", rowObject.getString("msg"));

                    Intent i = new Intent(SignUpActivity.this,LoginActivity.class);
                    i.putExtras(bundle);
                    startActivity(i);

                    finish();
                    return true;
                }
                else if(res.equals("error"))
                {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            try
                            {
                                Toast.makeText(context,rowObject.getString("msg"),Toast.LENGTH_SHORT).show();
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }

                        }
                    });
                    return false;

                }
            }
            catch (Exception e)
            {
                runOnUiThread(new Runnable() {
                    public void run() {
                        try
                        {
                            Toast.makeText(context,"Error: No internet",Toast.LENGTH_SHORT).show();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }
                });

                //showProgress(false);
                e.printStackTrace();
            }


            // TODO: register the new account here.
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(!aBoolean)
            {
                showProgress(aBoolean);
            }
            super.onPostExecute(aBoolean);
        }
    }
}


