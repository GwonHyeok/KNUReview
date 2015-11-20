package dev.knureview.Holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.unnamed.b.atv.model.TreeNode;

import dev.knureview.R;

/**
 * Created by DavidHa on 2015. 11. 16..
 */
public class DetailViewHolder extends TreeNode.BaseNodeViewHolder<DetailViewHolder.DetailViewItem> {


    public DetailViewHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(TreeNode node, DetailViewItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.layout_detail_node, null, false);

        TextView gradeName = (TextView) view.findViewById(R.id.gradeName);
        gradeName.setText(value.gradeName);
        TextView deptName = (TextView) view.findViewById(R.id.deptName);
        deptName.setText(value.deptName);
        return view;
    }

    @Override
    public void toggle(boolean active) {
    }


    public static class DetailViewItem {
        public String gradeName;
        public String deptName;

        public DetailViewItem(String gradeName) {
            this.gradeName = gradeName;
        }
        public DetailViewItem(String gradeName, String deptName) {
            this.gradeName = gradeName;
            this.deptName = deptName;
        }
    }

}