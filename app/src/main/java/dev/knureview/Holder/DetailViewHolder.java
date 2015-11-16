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
public class DetailViewHolder extends TreeNode.BaseNodeViewHolder<DetailViewHolder.SocialItem> {



    public DetailViewHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(TreeNode node, SocialItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.layout_detail_node, null, false);


        TextView userNameLabel = (TextView) view.findViewById(R.id.username);
        userNameLabel.setText(value.text);


        return view;
    }

    @Override
    public void toggle(boolean active) {
    }


    public static class SocialItem {
        public String text;

        public SocialItem(String text) {
            this.text = text;
        }
    }

}