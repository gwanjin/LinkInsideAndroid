package kita.example.com.kitkatproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    JSONObject mJson = null;
    JSONObject mItem = null;
    String mFlag;
    TextView mLoginResultText;
    EditText mLoginId;
    EditText mLoginPassword;
    Button mApplicantButton;
    Button mCompanyButton;
    CheckBox autoLogin;

    Handler mHandler;
    ProgressDialog mProgressDialog;

    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        activity = LoginActivity.this;
        mLoginId = (EditText)findViewById(R.id.loginId);
        mLoginPassword = (EditText)findViewById(R.id.loginPassword);
        mApplicantButton = (Button)findViewById(R.id.applicantLoginButton);
        mCompanyButton = (Button)findViewById(R.id.companyLoginButton);
        autoLogin = (CheckBox)findViewById(R.id.auto_login);
        mLoginResultText = (TextView)findViewById(R.id.loginResult);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        String id = pref.getString("id", "");


        if(!id.equals("")){
            mLoginId.setText(id);
            mLoginPassword.setText(pref.getString("password", ""));
            mFlag = pref.getString("flag", "");

            autoLogin.setChecked(true);
            sendData();
        }

        if(savedInstanceState != null){
            mLoginId.setText(savedInstanceState.getString("id"));
            mLoginPassword.setText(savedInstanceState.getString("password"));
        }


    }

    public void sendData(){
        String url = MainActivity.URL_ADDRESS;
        String id = mLoginId.getText().toString();
        String password = mLoginPassword.getText().toString();
        StringBuffer buffer = new StringBuffer();

        if(mFlag == null){
            new AlertDialog.Builder(this)
                    .setTitle("회원 타입 선택")
                    .setMessage("타입을 선택해주세요.")
                    .setIcon(R.mipmap.ic_launcher)
                    .setCancelable(false)
                    .setPositiveButton("닫기", null)
                    .show();
        }else{
            if(mFlag.equals("applicant")){
                buffer.append("applicant.applicantid").append("=").append(id).append("&");
                buffer.append("applicant.applicantpassword").append("=").append(password);
                url += "androidApplicantLogin.action";
            }else if(mFlag.equals("company")){
                buffer.append("company.compid").append("=").append(id).append("&");
                buffer.append("company.comppassword").append("=").append(password);
                url += "androidCompnayLogin.action";
            }

            StringBuilder result = NetworkConnect.send(url, buffer.toString());

            if(result == null){
                mLoginResultText.setText("다시 한번 입력해주세요.");
            }else {
                try{
                    mJson = new JSONObject(result.toString());
                    String value = mJson.getString(mFlag);
                    if(value.equals("null")){
                        mLoginResultText.setText("아이디와 비밀번호를 확인해주세요.");
                    }else{
                        mItem = new JSONObject(value);

                        if(autoLogin.isChecked()){
                            SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("id", id);
                            editor.putString("password", password);
                            editor.putString("flag", mFlag);
                            editor.commit();
                        }

                        Intent intent = new Intent(this, MainActivity.class);
                        intent.putExtra("flag", mFlag);
                        intent.putExtra("id", id);
                        startActivity(intent);
                    }
                }catch(JSONException e2){
                    e2.printStackTrace();
                }
            }
        }
    }

    public void mOnClick(View view) {
        sendData();
    }

    public void flagClick(View view) {
        switch(view.getId()){
            case R.id.applicantLoginButton:
                mFlag = "applicant";
                break;
            case R.id.companyLoginButton:
                mFlag = "company";
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        String id = mLoginId.getText().toString();
        String password = mLoginPassword.getText().toString();
        outState.putString("id", id);
        outState.putString("password", password);
        super.onSaveInstanceState(outState);
    }
}
