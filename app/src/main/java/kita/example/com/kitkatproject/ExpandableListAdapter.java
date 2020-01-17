package kita.example.com.kitkatproject;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by user on 2017-01-12.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    int pageNum;
    String status;

    private List<String> listDataHeader;
    private HashMap<String, List<String>> listHashMap;
    private ArrayList<HashMap<String, String>> listData;

    public ExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listHashMap,
                                                                ArrayList<HashMap<String, String>> listData, int pageNum) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listHashMap = listHashMap;
        this.listData = listData;
        this.pageNum = pageNum;
    }

    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listHashMap.get(listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listHashMap.get(listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(pageNum == 1) {
                convertView = inflater.inflate(R.layout.listgroup, null);

            }
            else {
                convertView = inflater.inflate(R.layout.listgroup_2, null);
            }
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.comp_name);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        TextView sidebar = (TextView) convertView.findViewById(R.id.sidebar);

        for(int i=0; i<listData.size(); i++){
            Iterator<String> iterator = listData.get(i).keySet().iterator();
            while(iterator.hasNext()){
                String key = (String)iterator.next();
                Log.d("Key value : ", key);
                Log.d("Real Value : ", listData.get(i).get(key));
            }
        }

        if(pageNum == 1){
            TextView percent = (TextView)convertView.findViewById(R.id.percent);
            percent.setText((listData.get(groupPosition).get("TOTALPER")) + "%");

            TextView titleDescription = (TextView)convertView.findViewById(R.id.contentdesc);
            titleDescription.setText(listData.get(groupPosition).get("JOBTITLE"));

            if(groupPosition % 2 == 0) {
                sidebar.setBackgroundColor(Color.rgb(131, 176, 250));
                percent.setTextColor(Color.rgb(131, 176, 250));
            } else {
                sidebar.setBackgroundColor(Color.rgb(255, 196, 244));
                percent.setTextColor(Color.rgb(255, 196, 244));
            }
        }else{
            TextView titleDescription = (TextView)convertView.findViewById(R.id.contentdesc);
            titleDescription.setText(listData.get(groupPosition).get("JOBTITLE"));

            ImageView yesNo = (ImageView)convertView.findViewById(R.id.yesno);
            status = listData.get(groupPosition).get("STATUS");
            if(status.equals("AP")){
                yesNo.setImageResource(R.drawable.green);
                sidebar.setBackgroundColor(Color.rgb(135, 204, 139));
            }else if(status.equals("AF")){
                yesNo.setImageResource(R.drawable.red);
                sidebar.setBackgroundColor(Color.RED);
            }else if(status.equals("SN")){
                yesNo.setImageResource(R.drawable.green);
                sidebar.setBackgroundColor(Color.GRAY);
            }else{
                yesNo.setImageDrawable(null);
                sidebar.setBackgroundColor(Color.GRAY);
            }
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(pageNum == 1)
                convertView = inflater.inflate(R.layout.listitem, null);
            else
                convertView = inflater.inflate(R.layout.listitem_2, null);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.image_view);
        View sidebar = (View) convertView.findViewById(R.id.child_sidebar);
        TextView top_bar = (TextView) convertView.findViewById(R.id.top_bar);

        if(pageNum  == 1){
            if(groupPosition % 2 == 0) {
                imageView.setBackgroundColor(Color.rgb(131, 176, 250));
                sidebar.setBackgroundColor(Color.rgb(131, 176, 250));
                top_bar.setBackgroundColor(Color.rgb(131, 176, 250));
            } else {
                imageView.setBackgroundColor(Color.rgb(255, 196, 244));
                sidebar.setBackgroundColor(Color.rgb(255, 196, 244));
                top_bar.setBackgroundColor(Color.rgb(255, 196, 244));
            }
        }else{
            if(status.equals("AP")){
                imageView.setBackgroundColor(Color.rgb(135, 204, 139));
                sidebar.setBackgroundColor(Color.rgb(135, 204, 139));
                top_bar.setBackgroundColor(Color.rgb(135, 204, 139));
            }else if(status.equals("AF")){
                imageView.setBackgroundColor(Color.RED);
                top_bar.setBackgroundColor(Color.RED);
                sidebar.setBackgroundColor(Color.RED);
            }else{
                imageView.setBackgroundColor(Color.GRAY);
                top_bar.setBackgroundColor(Color.GRAY);
                sidebar.setBackgroundColor(Color.GRAY);
            }
        }


        TextView sub_jobName = (TextView)convertView.findViewById(R.id.sub_jobname);
        TextView sub_jobDesc = (TextView)convertView.findViewById(R.id.sub_jobdesc);
        TextView sub_jobPeriod = (TextView)convertView.findViewById(R.id.sub_jobdate);
        TextView sub_edu = (TextView)convertView.findViewById(R.id.sub_edu);
        TextView sub_exp = (TextView)convertView.findViewById(R.id.sub_exp);
        TextView sub_skill = (TextView)convertView.findViewById(R.id.sub_skill);

        String eduText = null;

        String edulevel = listData.get(groupPosition).get("EDULEVEL");
        if(edulevel.equals("1")){
            eduText = "無関係";
        }else if(edulevel.equals("2")){
            eduText = "高専卒";
        }else if(edulevel.equals("3")){
            eduText = "専門卒";
        }else if(edulevel.equals("4")){
            eduText = "短大卒";
        }else if(edulevel.equals("5")){
            eduText = "大学卒";
        }else if(edulevel.equals("6")){
            eduText = "大学院卒";
        }

        sub_jobName.setText(listData.get(groupPosition).get("JOBTITLE"));
        sub_jobDesc.setText(listData.get(groupPosition).get("JOBDESC"));
        sub_jobPeriod.setText(listData.get(groupPosition).get("JOBSTART") + " ~ " + listData.get(childPosition).get("JOBEND"));
        sub_edu.setText(eduText);
        sub_exp.setText(listData.get(groupPosition).get("EXPAREA"));
        sub_skill.setText(listData.get(groupPosition).get("SKILLNAME"));




        /*TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
        txtListChild.setText(childText);*/

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }
}