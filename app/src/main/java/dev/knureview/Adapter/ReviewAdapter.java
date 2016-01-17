package dev.knureview.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import dev.knureview.R;
import dev.knureview.VO.ReviewVO;

/**
 * Created by DavidHa on 2016. 1. 17..
 */
public class ReviewAdapter extends ArrayAdapter<ReviewVO> {

    private Activity activity;
    private int layot;
    private ArrayList<ReviewVO> rvList;

    static class ViewHolder{
        @Bind(R.id.sbjNameTxt) TextView sbjNameTxt;
        @Bind(R.id.profNameTxt) TextView profNameTxt;
        @Bind(R.id.stdNoTxt) TextView stdNoTxt;
        @Bind(R.id.descriptionTxt) TextView descriptionTxt;
        @Bind(R.id.difcRating) RatingBar difcRating;
        @Bind(R.id.asignRating) RatingBar asignRating;
        @Bind(R.id.atendRating) RatingBar atendRating;
        @Bind(R.id.gradeRating) RatingBar gradeRating;
        @Bind(R.id.achivRating) RatingBar achivRating;

        ViewHolder(View view){
            ButterKnife.bind(this, view);
        }

    }

    public ReviewAdapter(Activity activity, int layout, ArrayList<ReviewVO> list){
        super(activity, layout, list);
        this.activity = activity;
        this.layot = layout;
        this.rvList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if(convertView==null){
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(layot, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder)convertView.getTag();
        }

        vh.sbjNameTxt.setText(rvList.get(position).getSbjName());
        vh.profNameTxt.setText(rvList.get(position).getProfName());
        vh.stdNoTxt.setText(String.valueOf(rvList.get(position).getStdNo()).substring(0,6)+"***");
        String description =rvList.get(position).getDescription();
        if(description.trim().equals("")){
            vh.descriptionTxt.setText("(리뷰 없음)");
        }else {
            vh.descriptionTxt.setText(description);
        }
        vh.difcRating.setRating(rvList.get(position).getDifc());

        vh.asignRating.setRating(rvList.get(position).getAsign());
        vh.atendRating.setRating(rvList.get(position).getAtend());
        vh.gradeRating.setRating(rvList.get(position).getGrade());
        vh.achivRating.setRating(rvList.get(position).getAchiv());
        return convertView;
    }
}
