package kita.example.com.kitkatproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import kita.example.com.kitkatproject.company.EventActivity;
import kita.example.com.kitkatproject.company.ListActivity;

/**
 * Created by kita on 2017-01-10.
 */

public class MainActivity extends AppCompatActivity {
    public static final String URL_ADDRESS = "http://192.168.11.6/linkinside/";
    public static String USERID;
    public static String mFlag;
    public static boolean mLoginFlag = true;


    LinkPagerAdapter mLinkPagerAdapter;
    ViewPager mViewPager;
    TabLayout mTabLayout;

    CalendarView cal;

    String month;
    String year;
    String day;



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void firstAction() {
        (new AsyncTask<MainActivity, Void, MainActivity>(){
            @Override
            protected MainActivity doInBackground(MainActivity... params) {
                return params[0];
            }

            @Override
            protected void onPostExecute(MainActivity mainActivity) {
                mLinkPagerAdapter = new LinkPagerAdapter(mainActivity.getSupportFragmentManager());
                mViewPager = (ViewPager)mainActivity.findViewById(R.id.view_pager);
                mViewPager.setAdapter(mLinkPagerAdapter);

                mTabLayout = (TabLayout)findViewById(R.id.tabs);
                mTabLayout.setupWithViewPager(mViewPager);
            }
        }).execute(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseMessaging.getInstance().subscribeToTopic("news");
        FirebaseInstanceId.getInstance().getToken();
        USERID = getIntent().getStringExtra("id");
        mFlag = getIntent().getStringExtra("flag");

        if(mFlag.equals("applicant")){  // applicant의 경우
            setContentView(R.layout.activity_main);
            closeLogin();
            firstAction();

        }else{  // company의 경우
            setContentView(R.layout.activity_company_main);
            closeLogin();
            cal = (CalendarView) findViewById(R.id.cal);
//            Button calbtn = (Button) this.findViewById(R.id.calbtn);
//            Button listbtn = (Button) this.findViewById(R.id.listbtn);
//            calbtn.setOnClickListener(null);

            cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){
                @Override
                public void onSelectedDayChange(CalendarView view, int c_year, int c_month, int c_day) {
                    month = Integer.toString(c_month+1);
                    year = Integer.toString(c_year);
                    day = Integer.toString(c_day);
//                    Toast.makeText(MainActivity.this, "year" + c_year + "/" + "month :" + c_month + "day: " + c_day, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void closeLogin(){
        if(mLoginFlag){
            LoginActivity la = (LoginActivity)LoginActivity.activity;
            la.finish();
            mLoginFlag = false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.logout:
                new AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("로그아웃 하시겠습니까?")
                    .setIcon(R.mipmap.ic_launcher)

                    .setNegativeButton("YES", new DialogInterface.OnClickListener() {   //   로그 아웃함
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.clear();
                            editor.commit();
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finish();
                        }
                    })
                    .setPositiveButton("NO", new DialogInterface.OnClickListener() {    // 로그 아웃 안함
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void mOnClick(View v){
        switch (v.getId()){
            case R.id.eventbtn:
                Intent intent = new Intent(this, EventActivity.class);
                if(year != null || month != null || day != null){
                    intent.putExtra("year", year);
                    intent.putExtra("month", month);
                    intent.putExtra("day", day);
                    startActivity(intent);
                }else{
                    Toast.makeText(this, "날짜가 선택되지 않았습니다", Toast.LENGTH_SHORT).show();
                }
                break;
//            case R.id.calbtn:
//
//                break;
//            case R.id.listbtn:
//                Intent m_intent = new Intent(this, ListActivity.class);
//                startActivity(m_intent);
//                break;

        }
    }
}
