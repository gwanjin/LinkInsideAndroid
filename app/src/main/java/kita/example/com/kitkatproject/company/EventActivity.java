package kita.example.com.kitkatproject.company;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import kita.example.com.kitkatproject.MainActivity;
import kita.example.com.kitkatproject.NetworkConnect;
import kita.example.com.kitkatproject.R;

/**
 * Created by user on 2017-01-17.
 */

public class EventActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.event_form);

        Bundle extra = getIntent().getExtras();
        StringBuilder sb = new StringBuilder();
        sb.append(extra.getString("year"));
        sb.append("/" + extra.getString("month"));
        sb.append("/" + extra.getString("day"));
        TextView textView = (TextView) this.findViewById(R.id.dateText);
        ((TextView) this.findViewById(R.id.dateText)).setText(sb.toString());
    }

    public void eventOnClick(View v){
        sendData();
        this.finish();
    }

    public void sendData(){
        String url = MainActivity.URL_ADDRESS;
        StringBuffer buffer = new StringBuffer();

        String day = ((TextView)findViewById(R.id.dateText)).getText().toString();
        String phoneText = ((EditText)findViewById(R.id.phoneText)).getText().toString();
        String location = ((EditText)findViewById(R.id.locationText)).getText().toString();
        String s_time = ((EditText)findViewById(R.id.startText)).getText().toString();
        String memo = ((EditText)findViewById(R.id.memoText)).getText().toString();

        buffer.append("phoneNumber").append("=").append(phoneText).append("&");
        buffer.append("schedule.day").append("=").append(day).append("&");
        buffer.append("schedule.location").append("=").append(location).append("&");
        buffer.append("schedule.s_time").append("=").append(s_time).append("&");
        buffer.append("schedule.memo").append("=").append(memo);

        url += "insertSchedule.action";

        NetworkConnect connect = new NetworkConnect();
        StringBuilder result = connect.send(url, buffer.toString());

    }
}
