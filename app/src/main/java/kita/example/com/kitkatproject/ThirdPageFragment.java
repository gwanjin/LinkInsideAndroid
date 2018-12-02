package kita.example.com.kitkatproject;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kita on 2017-01-10.
 */

public class ThirdPageFragment extends Fragment {
    StringBuilder result;
    JSONObject mJson = null;
    String[] mMonArr = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
    String clickJobid;

    public static ThirdPageFragment newInstance(){
        Bundle args = new Bundle();
        ThirdPageFragment thirdPageFragment = new ThirdPageFragment();
        thirdPageFragment.setArguments(args);
        return thirdPageFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_third, container, false);
        LinearLayout linearLayout = (LinearLayout)root.findViewById(R.id.third_page);

        sendData();

        try{
            mJson = new JSONObject(result.toString());
            JSONArray jsonArray = mJson.getJSONArray("result1");

            for(int i=0; i<jsonArray.length(); i++){
                JSONObject item = jsonArray.getJSONObject(i);
                String location = item.getString("LOCATION");
                String s_time = item.getString("S_TIME");
                String day = item.getString("DAY");
                String jobdesc = item.getString("JOBDESC");
                String jobtitle = item.getString("JOBTITLE");
                String[] split = day.split("-");
                String jobid = item.getString("JOBID");

                RelativeLayout relativeLayout = (RelativeLayout)View.inflate(root.getContext(), R.layout.activity_resource3, null);

                ImageView im = (ImageView)relativeLayout.findViewById(R.id.cal_pic);
                im.setOnClickListener(new MyClickListener(jobtitle, location, jobdesc, split, jobid));

                ((TextView)relativeLayout.findViewById(R.id.title)).setText(jobtitle);
                ((TextView)relativeLayout.findViewById(R.id.contentdesc)).setText(jobdesc);
                ((TextView)relativeLayout.findViewById(R.id.place)).setText(location);
                ((TextView)relativeLayout.findViewById(R.id.timeView)).setText(s_time);


                ((TextView)relativeLayout.findViewById(R.id.date)).setText(split[1]);
                ((TextView)relativeLayout.findViewById(R.id.month)).setText(mMonArr[Integer.parseInt(split[1])-1]);
                ((TextView)relativeLayout.findViewById(R.id.year)).setText(split[2]);



                linearLayout.addView(relativeLayout);
            }


        }catch(JSONException e2){
            e2.printStackTrace();
        }


        return root;
    }

    public void sendData(){
        String url = MainActivity.URL_ADDRESS;
        StringBuffer buffer = new StringBuffer();
        url += "findSchedule.action";
        buffer.append("userid").append("=").append(MainActivity.USERID);
        Log.d("Third userid " , MainActivity.USERID);
        result = NetworkConnect.send(url, buffer.toString());
    }

    class MyClickListener implements View.OnClickListener{
        String jobtitle;
        String location;
        String jobdesc;
        String split[];
        String jobid;

        MyClickListener(String jobtitle, String location, String jobdesc, String[] split, String jobid){
            this.jobtitle = jobtitle;
            this.jobdesc = jobdesc;
            this.location = location;
            this.split = split;
            this.jobid = jobid;
        }

        @Override
        public void onClick(View view) {
            clickJobid = jobid;
            Intent calIntent = new Intent (Intent.ACTION_INSERT);
            calIntent.setType("vnd.android.cursor.item/event");

            calIntent.putExtra(CalendarContract.Events.TITLE, jobtitle);
            calIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, location);
            calIntent.putExtra(CalendarContract.Events.DESCRIPTION, jobdesc);

            GregorianCalendar calDate = new GregorianCalendar(Integer.parseInt(split[0]), Integer.parseInt(split[1])-1, Integer.parseInt(split[2]));
            calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calDate.getTimeInMillis());

            calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                    calDate.getTimeInMillis());
            calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                    calDate.getTimeInMillis());
                /*calIntent.setData(CalendarContract.Events.CONTENT_URI);*/
            startActivityForResult(calIntent, 1);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        sendData2();
    }

    public void sendData2(){
        String url = MainActivity.URL_ADDRESS;
        StringBuffer buffer = new StringBuffer();
        url += "updateCal.action";
        buffer.append("userid").append("=").append(MainActivity.USERID).append("&");
        buffer.append("jobid").append("=").append(clickJobid);
        Log.d("Third userid " , MainActivity.USERID);
        result = NetworkConnect.send(url, buffer.toString());
    }
}
