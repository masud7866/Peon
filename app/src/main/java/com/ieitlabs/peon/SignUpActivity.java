package com.ieitlabs.peon;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.util.EntityUtils;
import cz.msebera.android.httpclient.util.TextUtils;

public class SignUpActivity extends AppCompatActivity {

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
        RadioGroup mRGCType = (RadioGroup)findViewById(R.id.ctype_radio_group);

        View radioButton = mRGCType.findViewById(mRGCType.getCheckedRadioButtonId());
        int idx = mRGCType.indexOfChild(radioButton);
        RadioButton r = (RadioButton)  mRGCType.getChildAt(idx);
        String OrgType = r.getText().toString();



        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
                finish();
            }
        });

        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mETOrgTitle.setError(null);
                mETEmail.setError(null);
                mPassword.setError(null);
                mETDesc.setError(null);

                if(TextUtils.isEmpty(mETOrgTitle.getText().toString()))
                {
                    mETOrgTitle.setError("Title can't be empty");
                    mETOrgTitle.requestFocus();
                }
                if(!isEmailValid(mETEmail.getText().toString()))
                {
                    mETEmail.setError("Invalid email");
                    mETEmail.requestFocus();
                }
                if(TextUtils.isEmpty(mPassword.getText().toString()))
                {
                    mPassword.setError("Minimum 4 characters");
                    mPassword.requestFocus();
                }
                if(TextUtils.isEmpty(mETDesc.getText().toString()))
                {
                    mETDesc.setError("Description can't be empty");
                    mETDesc.requestFocus();
                }

            }
        });

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

        public UserRegistration(String mEmail, String mPassword, String MOrgTitle, String MOrgType, String MOrgDesc) {
            this.mEmail = mEmail;
            this.mPassword = mPassword;
            this.MOrgTitle = MOrgTitle;
            this.MOrgType = MOrgType;
            this.MOrgDesc = MOrgDesc;
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

            String url="http://peon.ml/api/orgrequest?title=" + MOrgTitle + "&type=" + MOrgType  + "&email=" + mEmail + "&pass=" + mPassword  + "&desc=" + MOrgDesc;

            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(url);

            try {
                HttpResponse response = client.execute(request);
                HttpEntity entity = response.getEntity();
                String content = EntityUtils.toString(entity);
                // JSONObject jsonArray = new JSONObject(content);
                final JSONObject rowObject = new JSONObject(content);
                String res = rowObject.getString("response");
                if(res.equals("success"))
                {
                    String sucCode = rowObject.getString("sucCode");
                   // startActivity(new Intent(LoginActivity.this,SideBar.class));
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

            super.onPostExecute(aBoolean);
        }
    }
}


