package kita.example.com.kitkatproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kita on 2017-01-10.
 */

public class SecondPageFragment extends Fragment {
    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private ArrayList<HashMap<String, String>> listData;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listHash;

    JSONObject mJson = null;
    JSONObject mItem = null;
    StringBuilder result;

    public static SecondPageFragment newInstance(){
        Bundle args = new Bundle();
        SecondPageFragment secondPageFragment = new SecondPageFragment();
        secondPageFragment.setArguments(args);
        return secondPageFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = null;
        sendData();

       try{
            mJson = new JSONObject(result.toString());
            JSONArray jsonArray = mJson.getJSONArray("result1");

            if(jsonArray.length() == 0){
                root = inflater.inflate(R.layout.activty_noinfo, container, false);
            }else{
                root = inflater.inflate(R.layout.activity_second, container, false);
                listView = (ExpandableListView)root.findViewById(R.id.exp_view2);
                initData(jsonArray);
                listAdapter = new ExpandableListAdapter(root.getContext(), listDataHeader, listHash, listData, 2);
                listView.setAdapter(listAdapter);

                listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                    int previousItem = -1;

                    @Override
                    public void onGroupExpand(int groupPosition) {
                        if(groupPosition != previousItem )
                            listView.collapseGroup(previousItem );
                        previousItem = groupPosition;
                    }
                });
            }

        }catch(JSONException e2){
            e2.printStackTrace();
        }

        return root;
    }



    public void sendData(){
        String url = MainActivity.URL_ADDRESS;
        StringBuffer buffer = new StringBuffer();
        url += "androidApplyList.action";
        buffer.append("userid").append("=").append(MainActivity.USERID);

        result = NetworkConnect.send(url, buffer.toString());
    }

    private void initData(JSONArray jsonArray) {
        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();
        listData = new ArrayList<>();
        try{
            for(int i=0; i<jsonArray.length(); i++){
                JSONObject item = jsonArray.getJSONObject(i);

                listDataHeader.add(item.getString("JOBTITLE"));
                List<String> a = new ArrayList<>();
                a.add("1");
                listHash.put(listDataHeader.get(i), a);

                HashMap<String, String> data = new HashMap<>();
                data.put("STATUS", item.getString("STATUS"));
                data.put("JOBTITLE", item.getString("JOBTITLE"));
                data.put("JOBDESC", item.getString("JOBDESC"));
                data.put("JOBSTART", item.getString("JOBSTART"));
                data.put("JOBEND", item.getString("JOBEND"));
                data.put("EDULEVEL", item.getString("EDULEVEL"));
                data.put("EXPAREA", item.getString("EXPAREA"));

                listData.add(data);
            }

        }catch(JSONException e){
            e.printStackTrace();
        }
    }
}
