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
        addCollegeOfCulture(cNode1);
        //부총장직속학부 add 안함
        addCollegeOfHumanities(cNode3);
        addCollegeOfInternationalStudies(cNode4);
        addCollegeOfSinology(cNode5);
        addCollegeOfEducation(cNode6);
        addCollegeOfSocialScience(cNode7);
        addCollegeOfSocialWelfare(cNode8);
        addCollegeOfManagement(cNode9);
        addCollegeOfTech(cNode10);
        addCollegeOfArtsAndPhysical(cNode11);
        //미래인재개발대학 add 안함

        root.addChildren(cNode1, cNode3, cNode4, cNode5, cNode6,
                cNode7, cNode8, cNode9, cNode10, cNode11);

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

    //교양선택
    private void addCollegeOfCulture(TreeNode parentNode) {
        TreeNode childNode1 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_bookmark_outline, "교양선택(주)"))
                .setViewHolder(new DepartmentHolder(getActivity()));
        TreeNode childNode2 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_bookmark_outline, "교양선택(야)"))
                .setViewHolder(new DepartmentHolder(getActivity()));
        addCultureData(childNode1);
        addCultureData(childNode2);
        parentNode.addChildren(childNode1, childNode2);
    }


    //인문대학
    private void addCollegeOfHumanities(TreeNode parentNode) {
        TreeNode childNode0 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "인문대학 1학년 교양필수"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        TreeNode childNode1 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "신학과"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        TreeNode childNode2 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "철학과"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        TreeNode childNode3 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "국어국문학과"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        TreeNode childNode4 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "문헌정보학과"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        TreeNode childNode5 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "영문학과"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        addGradeData(childNode1);
        addGradeData(childNode2);
        addGradeData(childNode3);
        addGradeData(childNode4);
        addGradeData(childNode5);
        parentNode.addChildren(childNode0, childNode1, childNode2, childNode3, childNode4, childNode5);
    }

    //국제학대학
    private void addCollegeOfInternationalStudies(TreeNode parentNode) {
        TreeNode childNode0 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "국제학대학 1학년 교양필수"))
                .setViewHolder(new DepartmentHolder(getActivity()));
        TreeNode childNode1 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "국제지역학부"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        TreeNode childNode2 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "국제통상학과"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        addGradeData(childNode1);
        addGradeData(childNode2);
        parentNode.addChildren(childNode0, childNode1, childNode2);
    }

    //중국학대학
    private void addCollegeOfSinology(TreeNode parentNode) {
        TreeNode childNode0 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "중국학대학 1학년 교양필수"))
                .setViewHolder(new DepartmentHolder(getActivity()));
        TreeNode childNode1 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "중국어문화학과"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        TreeNode childNode2 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "중국실용지역학과"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        addGradeData(childNode1);
        addGradeData(childNode2);
        parentNode.addChildren(childNode0, childNode1, childNode2);
    }

    //사범대학
    private void addCollegeOfEducation(TreeNode parentNode) {
        TreeNode childNode0 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "사범대학 1학년 교양필수"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        TreeNode childNode1 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "교육학과"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        TreeNode childNode2 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "유아교육과"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        TreeNode childNode3 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "초등특수교육"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        TreeNode childNode4 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "중등특수교육"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        addGradeData(childNode1);
        addGradeData(childNode2);
        addGradeData(childNode3);
        addGradeData(childNode4);
        parentNode.addChildren(childNode0, childNode1, childNode2, childNode3, childNode4);
    }

    //사회과학대학
    private void addCollegeOfSocialScience(TreeNode parentNode) {
        TreeNode childNode0 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "사회과학대학 1학년 교양필수"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        TreeNode childNode1 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "경제학과"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        TreeNode childNode2 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "법학과"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        TreeNode childNode3 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "행정학과"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        TreeNode childNode4 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "부동산학과"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        TreeNode childNode5 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "세무학과"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        addGradeData(childNode1);
        addGradeData(childNode2);
        addGradeData(childNode3);
        addGradeData(childNode4);
        addGradeData(childNode5);
        parentNode.addChildren(childNode0, childNode1, childNode2, childNode3, childNode4, childNode5);
    }

    //사회복지대학
    private void addCollegeOfSocialWelfare(TreeNode parentNode) {
        TreeNode childNode0 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "사회복지대학 1학년 교양필수"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        TreeNode childNode1 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "사회복지학부"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        addGradeData(childNode1);
        parentNode.addChildren(childNode0, childNode1);
    }

    //경영대학
    private void addCollegeOfManagement(TreeNode parentNode) {
        TreeNode childNode0 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "경영대학 1학년 교양필수"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        TreeNode childNode1 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "경영학부"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        addGradeData(childNode1);
        parentNode.addChildren(childNode0, childNode1);
    }

    //공과대학
    private void addCollegeOfTech(TreeNode parentNode) {
        TreeNode childNode0 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "공과대학 1학년 교양필수"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        TreeNode childNode1 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "컴퓨터미디어정보공학부"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        TreeNode childNode2 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "전자공학과"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        TreeNode childNode3 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "산업시스템공학과"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        TreeNode childNode4 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "응용수학과"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        TreeNode childNode5 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "도시공학과"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        TreeNode childNode6 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "건축공학과"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        addGradeData(childNode1);
        addGradeData(childNode2);
        addGradeData(childNode3);
        addGradeData(childNode4);
        addGradeData(childNode5);
        addGradeData(childNode6);
        parentNode.addChildren(childNode0, childNode1, childNode2, childNode3, childNode4, childNode5, childNode6);
    }

    //예체능 대학
    private void addCollegeOfArtsAndPhysical(TreeNode parentNode) {
        TreeNode childNode0 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "예체능대학 1학년 교양필수"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        TreeNode childNode1 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "회화·디자인학부"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        TreeNode childNode2 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "음악학과"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        TreeNode childNode3 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "사회체육학과"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        addGradeData(childNode1);
        addGradeData(childNode2);
        addGradeData(childNode3);
        parentNode.addChildren(childNode0, childNode1, childNode2, childNode3);
    }

    //미래인재개발대학
    private void addCollegeOfFutureTalentDevelopment(TreeNode parentNode) {
        TreeNode childNode0 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "미래인재개발대학 1학년 교양필수"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        TreeNode childNode1 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "신산업융합학부"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        TreeNode childNode2 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "실버산업공학과"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        TreeNode childNode3 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "유니버설디자인공학과"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        TreeNode childNode4 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "금융정보학과"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        addGradeData(childNode1);
        addGradeData(childNode2);
        addGradeData(childNode3);
        addGradeData(childNode4);
        parentNode.addChildren(childNode0, childNode1, childNode2, childNode3, childNode4);
    }

    //교양 과목 표시
    private void addCultureData(TreeNode parentNode) {

        TreeNode cultureChildNode1 = new TreeNode(new DetailViewHolder.SocialItem("제 1영역(인문학)"))
                .setViewHolder(new DetailViewHolder(getActivity()));
        TreeNode cultureChildNode2 = new TreeNode(new DetailViewHolder.SocialItem("제 2영역(사회과학)"))
                .setViewHolder(new DetailViewHolder(getActivity()));
        TreeNode cultureChildNode3 = new TreeNode(new DetailViewHolder.SocialItem("제 3영역(자연과학)"))
                .setViewHolder(new DetailViewHolder(getActivity()));
        TreeNode cultureChildNode4 = new TreeNode(new DetailViewHolder.SocialItem("제 4영역(생활과예술)"))
                .setViewHolder(new DetailViewHolder(getActivity()));
        TreeNode cultureChildNode5 = new TreeNode(new DetailViewHolder.SocialItem("제 5영역(리더십과봉사)"))
                .setViewHolder(new DetailViewHolder(getActivity()));
        TreeNode cultureChildNode6 = new TreeNode(new DetailViewHolder.SocialItem("제 6영역(국제화)"))
                .setViewHolder(new DetailViewHolder(getActivity()));
        parentNode.addChildren(cultureChildNode1, cultureChildNode2,
                cultureChildNode3, cultureChildNode4,
                cultureChildNode5, cultureChildNode6);
    }

    //학년 표시
    private void addGradeData(TreeNode parentNode) {

        TreeNode gradeChildNode1 = new TreeNode(new DetailViewHolder.SocialItem("1학년"))
                .setViewHolder(new DetailViewHolder(getActivity()));
        TreeNode gradeChildNode2 = new TreeNode(new DetailViewHolder.SocialItem("2학년"))
                .setViewHolder(new DetailViewHolder(getActivity()));
        TreeNode gradeChildNode3 = new TreeNode(new DetailViewHolder.SocialItem("3학년"))
                .setViewHolder(new DetailViewHolder(getActivity()));
        TreeNode gradeChildNode4 = new TreeNode(new DetailViewHolder.SocialItem("4학년"))
                .setViewHolder(new DetailViewHolder(getActivity()));

        parentNode.addChildren(gradeChildNode1, gradeChildNode2, gradeChildNode3, gradeChildNode4);
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
