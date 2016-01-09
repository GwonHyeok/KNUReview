package dev.knureview.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import dev.knureview.R;
import dev.knureview.VO.GradeVO;
import dev.knureview.VO.SubjectVO;

/**
 * Created by DavidHa on 2016. 1. 4..
 */
public class SubjectAdapter extends ArrayAdapter<SubjectVO> {
    private Activity activity;
    private int layout;
    private ArrayList<SubjectVO> sbjList;
    private String deptName;
    private HashMap<Integer, String> dPositionHashMap;
    private int[] gradePosition;

    static class ViewHolder {
        private LinearLayout deptLayout;
        private TextView gradeTxt;
        private TextView dNameTxt;
        private TextView sNameTxt;
        private TextView creditTxt;
        private TextView timeTxt;
        private View row;
    }


    public SubjectAdapter(Activity activity, int layout, ArrayList<SubjectVO> sbjList) {
        super(activity, layout, sbjList);
        this.activity = activity;
        this.layout = layout;
        this.sbjList = sbjList;
        gradePosition = new int[4];
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
        dPositionHashMap = new HashMap<>();
        dPositionHashMap.put(0, "1학년");
        gradePosition[0] = 0;
        int cnt = 1;
        for (int i = 0; i < sbjList.size() - 1; i++) {
            if (sbjList.get(i).getGrade() != sbjList.get(i + 1).getGrade()) {
                cnt++;
                gradePosition[cnt-1] = i;
                dPositionHashMap.put(i + 1, cnt + "학년");
            }
        }
    }
    public int getGradePosition(String gradeName){
        if(gradeName.contains("1")){
            return gradePosition[0];
        }else if(gradeName.contains("2")){
            return gradePosition[1]+1;
        }else if(gradeName.contains("3")){
            return gradePosition[2]+1;
        }else if(gradeName.contains("4")){
            return gradePosition[3]+1;
        }
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;

        if (convertView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(layout, parent, false);
            vh = new ViewHolder();
            vh.deptLayout = (LinearLayout) convertView.findViewById(R.id.deptLayout);
            vh.dNameTxt = (TextView) convertView.findViewById(R.id.dNameTxt);
            vh.gradeTxt = (TextView) convertView.findViewById(R.id.gradeTxt);
            vh.sNameTxt = (TextView) convertView.findViewById(R.id.sNameTxt);
            vh.creditTxt = (TextView) convertView.findViewById(R.id.creditTxt);
            vh.timeTxt = (TextView) convertView.findViewById(R.id.timeTxt);
            vh.row = (View)convertView.findViewById(R.id.row);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        try {
            if (dPositionHashMap.get(position).contains("학년")) {
                vh.deptLayout.setVisibility(View.VISIBLE);
                vh.row.setVisibility(View.INVISIBLE);
                vh.dNameTxt.setText("("+deptName+")");
                vh.gradeTxt.setText(dPositionHashMap.get(position));
            } else {
                vh.deptLayout.setVisibility(View.GONE);
                vh.row.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            vh.deptLayout.setVisibility(View.GONE);
            vh.row.setVisibility(View.VISIBLE);
        }

        vh.sNameTxt.setText(sbjList.get(position).getsName());
        vh.timeTxt.setText(sbjList.get(position).getTime()+"시간");
        vh.creditTxt.setText(sbjList.get(position).getCredit()+"학점");


        return convertView;
    }

}
