package dev.knureview.Adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import dev.knureview.R;
import dev.knureview.VO.StudentVO;
import dev.knureview.VO.SubjectVO;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by DavidHa on 2016. 1. 4..
 */
public class SubjectAdapter extends ArrayAdapter<SubjectVO> implements StickyListHeadersAdapter{

    public SubjectAdapter(Activity activity, int layout, ArrayList<SubjectVO> sbjList ){
        super(activity, layout, sbjList);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    @Override
    public View getHeaderView(int i, View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public long getHeaderId(int i) {
        return 0;
    }
}
