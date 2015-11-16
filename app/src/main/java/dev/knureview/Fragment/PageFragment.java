package dev.knureview.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import dev.knureview.Holder.DepartmentHolder;
import dev.knureview.Holder.IconTreeItemHolder;
import dev.knureview.Holder.PlaceHolderHolder;
import dev.knureview.Holder.CollegeHolder;
import dev.knureview.Holder.DetailViewHolder;
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

        TreeNode cNode1 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "교양선택")).setViewHolder(new CollegeHolder(getActivity()));
        TreeNode cNode2 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "부총장직속학부")).setViewHolder(new CollegeHolder(getActivity()));
        TreeNode cNode3 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "인문대학")).setViewHolder(new CollegeHolder(getActivity()));
        TreeNode cNode4 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "국제학대학")).setViewHolder(new CollegeHolder(getActivity()));
        TreeNode cNode5 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "중국학대학")).setViewHolder(new CollegeHolder(getActivity()));
        TreeNode cNode6 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "사범대학")).setViewHolder(new CollegeHolder(getActivity()));
        TreeNode cNode7 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "사회과학대학")).setViewHolder(new CollegeHolder(getActivity()));
        TreeNode cNode8 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "사회복지대학")).setViewHolder(new CollegeHolder(getActivity()));
        TreeNode cNode9 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "경영대학")).setViewHolder(new CollegeHolder(getActivity()));
        TreeNode cNode10 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "공과대학")).setViewHolder(new CollegeHolder(getActivity()));
        TreeNode cNode11 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "예·체능대학")).setViewHolder(new CollegeHolder(getActivity()));
        TreeNode cNode12 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "미래인재개발대학")).setViewHolder(new CollegeHolder(getActivity()));
        addCollegeData(cNode1);
        addCollegeData(cNode2);
        addCollegeData(cNode3);
        addCollegeData(cNode4);
        addCollegeData(cNode5);
        addCollegeData(cNode6);
        addCollegeData(cNode7);
        addCollegeData(cNode8);
        addCollegeData(cNode9);
        addCollegeData(cNode10);
        addCollegeData(cNode11);
        addCollegeData(cNode12);

        root.addChildren(cNode1, cNode2, cNode3, cNode4, cNode5, cNode6, cNode7, cNode8, cNode9, cNode10, cNode11, cNode12);

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

    private void addCollegeData(TreeNode parentNode) {
        TreeNode cNode1_child1 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_bookmark_outline, "교양선택(주)")).setViewHolder(new DepartmentHolder(getActivity()));
        TreeNode cNode1_child2 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_bookmark_outline, "교양선택(야)")).setViewHolder(new DepartmentHolder(getActivity()));
        addCultureData(cNode1_child1);
        addCultureData(cNode1_child2);
        //TreeNode lake = new TreeNode(new IconTreeItemHolder.IconTreeItem("교양선택(주)")).setViewHolder(new PlaceHolderHolder(getActivity()));
        //TreeNode mountains = new TreeNode(new PlaceHolderHolder.PlaceItem("The white house")).setViewHolder(new PlaceHolderHolder(getActivity()));

        //cNode1_child1.addChildren(lake, mountains);
        // cNode1_child1.addChildren(lake);
        // cNode1_child2.addChildren(lake);
        parentNode.addChildren(cNode1_child1, cNode1_child2);
    }

    private void addGradeData(TreeNode parentNode) {

    }

    private void addCultureData(TreeNode parentNode) {

        TreeNode cultureChildNode1 = new TreeNode(new DetailViewHolder.SocialItem("제 1영역(인문학)")).setViewHolder(new DetailViewHolder(getActivity()));
        TreeNode cultureChildNode2 = new TreeNode(new DetailViewHolder.SocialItem("제 2영역(사회과학)")).setViewHolder(new DetailViewHolder(getActivity()));
        TreeNode cultureChildNode3 = new TreeNode(new DetailViewHolder.SocialItem("제 3영역(자연과학)")).setViewHolder(new DetailViewHolder(getActivity()));
        TreeNode cultureChildNode4 = new TreeNode(new DetailViewHolder.SocialItem("제 4영역(생활과예술)")).setViewHolder(new DetailViewHolder(getActivity()));
        TreeNode cultureChildNode5 = new TreeNode(new DetailViewHolder.SocialItem("제 5영역(리더십과봉사)")).setViewHolder(new DetailViewHolder(getActivity()));
        TreeNode cultureChildNode6 = new TreeNode(new DetailViewHolder.SocialItem("제 6영역(국제화)")).setViewHolder(new DetailViewHolder(getActivity()));
        parentNode.addChildren(cultureChildNode1, cultureChildNode2,
                cultureChildNode3, cultureChildNode4,
                cultureChildNode5, cultureChildNode6);
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
