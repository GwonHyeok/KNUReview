package dev.knureview.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import dev.knureview.Holder.HeaderHolder;
import dev.knureview.Holder.IconTreeItemHolder;
import dev.knureview.Holder.PlaceHolderHolder;
import dev.knureview.Holder.ProfileHolder;
import dev.knureview.Holder.SocialViewHolder;
import dev.knureview.R;

/**
 * Created by DavidHa on 2015. 11. 16..
 */
public class PageFragment extends android.support.v4.app.Fragment {
    private AndroidTreeView tView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_page, null, false);
        final ViewGroup containerView = (ViewGroup) rootView.findViewById(R.id.container);
        rootView.findViewById(R.id.status_bar).setVisibility(View.GONE);

        final TreeNode root = TreeNode.root();

        TreeNode myProfile = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_person, "My Profile")).setViewHolder(new ProfileHolder(getActivity()));
        TreeNode bruce = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_person, "Bruce Wayne")).setViewHolder(new ProfileHolder(getActivity()));
        TreeNode clark = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_person, "Clark Kent")).setViewHolder(new ProfileHolder(getActivity()));
        TreeNode barry = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_person, "Barry Allen")).setViewHolder(new ProfileHolder(getActivity()));
        addProfileData(myProfile);
        addProfileData(clark);
        addProfileData(bruce);
        addProfileData(barry);

        root.addChildren(myProfile, bruce, barry, clark);

        tView = new AndroidTreeView(getActivity(), root);
        tView.setDefaultAnimation(true);
        tView.setDefaultContainerStyle(R.style.TreeNodeStyleDivided, true);
        containerView.addView(tView.getView());

        if (savedInstanceState != null) {
            String state = savedInstanceState.getString("tState");
            if (!TextUtils.isEmpty(state)) {
                tView.restoreState(state);
            }
        }

        return rootView;
    }

    private void addProfileData(TreeNode profile) {
        TreeNode socialNetworks = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_people, "Social")).setViewHolder(new HeaderHolder(getActivity()));
        TreeNode places = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_place, "Places")).setViewHolder(new HeaderHolder(getActivity()));

        TreeNode facebook = new TreeNode(new SocialViewHolder.SocialItem(R.string.ic_post_facebook)).setViewHolder(new SocialViewHolder(getActivity()));
        TreeNode linkedin = new TreeNode(new SocialViewHolder.SocialItem(R.string.ic_post_linkedin)).setViewHolder(new SocialViewHolder(getActivity()));
        TreeNode google = new TreeNode(new SocialViewHolder.SocialItem(R.string.ic_post_gplus)).setViewHolder(new SocialViewHolder(getActivity()));
        TreeNode twitter = new TreeNode(new SocialViewHolder.SocialItem(R.string.ic_post_twitter)).setViewHolder(new SocialViewHolder(getActivity()));

        TreeNode lake = new TreeNode(new PlaceHolderHolder.PlaceItem("A rose garden")).setViewHolder(new PlaceHolderHolder(getActivity()));
        TreeNode mountains = new TreeNode(new PlaceHolderHolder.PlaceItem("The white house")).setViewHolder(new PlaceHolderHolder(getActivity()));

        places.addChildren(lake, mountains);
        socialNetworks.addChildren(facebook, google, twitter, linkedin);
        profile.addChildren(socialNetworks, places);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       // int position = FragmentPagerItem.getPosition(getArguments());
        // TextView title = (TextView) view.findViewById(R.id.item_title);
        //title.setText(String.valueOf(position));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tState", tView.getSaveState());
    }
}
