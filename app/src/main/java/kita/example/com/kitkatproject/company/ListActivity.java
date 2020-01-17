package kita.example.com.kitkatproject.company;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import kita.example.com.kitkatproject.MainActivity;
import kita.example.com.kitkatproject.NetworkConnect;
import kita.example.com.kitkatproject.R;

/**
 * Created by user on 2017-01-17.
 */

public class ListActivity extends Activity{
    StringBuilder result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list);

        sendData();
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.event_list);

    }

    public void mOnClick(View v){
        switch (v.getId()){
            case R.id.calbtn:
                finish();
            break;
            case R.id.listbtn:
                break;
        }
    }

    public void sendData(){
        String url = MainActivity.URL_ADDRESS;
        StringBuffer buffer = new StringBuffer();
        url += "getCompanySchedule.action";
        buffer.append("company.compid").append("=").append(MainActivity.USERID);
        Log.d("Third userid " , MainActivity.USERID);
        result = NetworkConnect.send(url, buffer.toString());
    }
}
