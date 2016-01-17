package dev.knureview.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import dev.knureview.Activity.SubjectActivity;
import dev.knureview.Holder.DepartmentHolder;
import dev.knureview.Holder.IconTreeItemHolder;
import dev.knureview.Holder.CollegeHolder;
import dev.knureview.Holder.DetailViewHolder;
import dev.knureview.R;
import dev.knureview.Util.SharedPreferencesActivity;

/**
 * Created by DavidHa on 2015. 11. 16..
 */
public class PageFragment extends android.support.v4.app.Fragment {
    private AndroidTreeView tView;
    private int selectedTerm;
    private String term;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SharedPreferencesActivity pref = new SharedPreferencesActivity(getContext());
        term = pref.getPreferences("term", "");

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
        //TreeNode cNode11 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "예·체능대학")).setViewHolder(new CollegeHolder(getActivity()));

        addCollegeOfCulture(cNode1);
        addCollegeOfViceChancellor(cNode2);
        addCollegeOfHumanities(cNode3);
        addCollegeOfInternationalStudies(cNode4);
        addCollegeOfSinology(cNode5);
        addCollegeOfEducation(cNode6);
        addCollegeOfSocialScience(cNode7);
        addCollegeOfSocialWelfare(cNode8);
        addCollegeOfManagement(cNode9);
        addCollegeOfTech(cNode10);
        //addCollegeOfArtsAndPhysical(cNode11);


        root.addChildren(cNode1, cNode2, cNode3, cNode4, cNode5, cNode6,
                cNode7, cNode8, cNode9, cNode10);

        tView = new AndroidTreeView(getActivity(), root);
        tView.setDefaultAnimation(true);
        tView.setDefaultContainerStyle(R.style.TreeNodeStyleDivided, true);
        tView.setDefaultNodeClickListener(nodeClickListener);
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
        TreeNode childNode2 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_bookmark_outline, "1학년 교양필수"))
                .setViewHolder(new DepartmentHolder(getActivity()));
        addCultureData(childNode1);
        addFirstGradeCultureData(childNode2);
        parentNode.addChildren(childNode1, childNode2);
    }

    //부총장직속학부
    private void addCollegeOfViceChancellor(TreeNode parentNode) {
        TreeNode childNode1 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark, "실버산업학부"))
                .setViewHolder(new DepartmentHolder(getActivity()));
        addDetailOfSilverIndustry(childNode1);
        parentNode.addChildren(childNode1);
    }

    private void addDetailOfSilverIndustry(TreeNode parentNode) {
        TreeNode childNode1 = new TreeNode(new DetailViewHolder.DetailViewItem("1학년", "(실버산업학부)"))
                .setViewHolder(new DetailViewHolder(getActivity()));

        TreeNode childNode2 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "실버산업학전공"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        addGradeData(childNode2, "(실버산업학전공)", 1);
        parentNode.addChildren(childNode1, childNode2);
    }


    //인문대학
    private void addCollegeOfHumanities(TreeNode parentNode) {


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

        addGradeData(childNode1, "(신학과)");
        addGradeData(childNode2, "(철학과)");
        addGradeData(childNode3, "(국어국문학과)");
        addGradeData(childNode4, "(문헌정보학과)");
        addGradeData(childNode5, "(영문학과)");
        parentNode.addChildren(childNode1, childNode2, childNode3, childNode4, childNode5);
    }

    //국제학대학
    private void addCollegeOfInternationalStudies(TreeNode parentNode) {

        TreeNode childNode1 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "국제통상학과"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        addGradeData(childNode1, "(국제통상학과)");
        parentNode.addChildren(childNode1);
    }


    //중국학대학
    private void addCollegeOfSinology(TreeNode parentNode) {

        TreeNode childNode1 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "중국어문화학과"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        TreeNode childNode2 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "중국실용지역학과"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        addGradeData(childNode1, "(중국어문화학과)");
        addGradeData(childNode2, "(중국실용지역학과)");
        parentNode.addChildren(childNode1, childNode2);
    }

    //사범대학
    private void addCollegeOfEducation(TreeNode parentNode) {

        TreeNode childNode1 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "교육학과"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        TreeNode childNode2 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "유아교육과"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        TreeNode childNode3 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "초등특수교육과"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        TreeNode childNode4 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "중등특수교육과"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        addGradeData(childNode1, "(교육학과)");
        addGradeData(childNode2, "(유아교육과)");
        addGradeData(childNode3, "(초등특수교육과)");
        addGradeData(childNode4, "(중등특수교육과)");
        parentNode.addChildren(childNode1, childNode2, childNode3, childNode4);
    }

    //사회과학대학
    private void addCollegeOfSocialScience(TreeNode parentNode) {

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

        addGradeData(childNode1, "(경제학과)");
        addGradeData(childNode2, "(법학과)");
        addGradeData(childNode3, "(행정학과)");
        addGradeData(childNode4, "(부동산학과)");
        addGradeData(childNode5, "(세무학과)");
        parentNode.addChildren(childNode1, childNode2, childNode3, childNode4, childNode5);
    }

    //사회복지대학
    private void addCollegeOfSocialWelfare(TreeNode parentNode) {

        TreeNode childNode1 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark, "사회복지학부"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        addDetailOfSocialWelfare(childNode1);
        parentNode.addChildren(childNode1);
    }

    //사회복지학부
    private void addDetailOfSocialWelfare(TreeNode parentNode) {
        TreeNode childNode1 = new TreeNode(new DetailViewHolder.DetailViewItem("1학년", "(사회복지학부)"))
                .setViewHolder(new DetailViewHolder(getActivity()));
        TreeNode childNode2 = new TreeNode(new DetailViewHolder.DetailViewItem("2학년", "(사회복지학부)"))
                .setViewHolder(new DetailViewHolder(getActivity()));

        TreeNode childNode3 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "사회사업학전공"))
                .setViewHolder(new DepartmentHolder(getActivity()));


        TreeNode childNode4 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "노인복지학전공"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        addGradeData(childNode3, "(사회사업학전공)", 2);
        addGradeData(childNode4, "(노인복지학전공)", 2);
        parentNode.addChildren(childNode1, childNode2, childNode3, childNode4);
    }

    //경영대학
    private void addCollegeOfManagement(TreeNode parentNode) {

        TreeNode childNode1 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark, "경영학부"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        addDetailOfManagement(childNode1);
        parentNode.addChildren(childNode1);
    }

    //경영학부
    private void addDetailOfManagement(TreeNode parentNode) {
        TreeNode childNode1 = new TreeNode(new DetailViewHolder.DetailViewItem("1학년", "(경영학부)"))
                .setViewHolder(new DetailViewHolder(getActivity()));
        TreeNode childNode2 = new TreeNode(new DetailViewHolder.DetailViewItem("2학년", "(경영학부)"))
                .setViewHolder(new DetailViewHolder(getActivity()));
        TreeNode childNode3 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "경영학전공"))
                .setViewHolder(new DepartmentHolder(getActivity()));
        addGradeData(childNode3, "(경영학전공)", 2);
        parentNode.addChildren(childNode1, childNode2, childNode3);
    }

    //공과대학
    private void addCollegeOfTech(TreeNode parentNode) {

        TreeNode childNode1 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark, "컴퓨터미디어정보공학부"))
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

        addDetailOfComputerEngineering(childNode1);
        addGradeData(childNode2, "(전자공학과)");
        addGradeData(childNode3, "(산업시스템공학과)");
        addGradeData(childNode4, "(응용수학과)");
        addGradeData(childNode5, "(도시공학과)");
        addGradeData(childNode6, "(건축공학과)");
        parentNode.addChildren(childNode1, childNode2, childNode3, childNode4, childNode5, childNode6);
    }

    //컴퓨터미디어정보공학부
    private void addDetailOfComputerEngineering(TreeNode parentNode) {

        TreeNode childNode1 = new TreeNode(new DetailViewHolder.DetailViewItem("1학년", "(컴퓨터미디어정보공학부)"))
                .setViewHolder(new DetailViewHolder(getActivity()));
        TreeNode childNode2 = new TreeNode(new DetailViewHolder.DetailViewItem("2학년", "(컴퓨터미디어정보공학부)"))
                .setViewHolder(new DetailViewHolder(getActivity()));

        TreeNode childNode3 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "컴퓨터공학전공"))
                .setViewHolder(new DepartmentHolder(getActivity()));


        TreeNode childNode4 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "미디어정보공학전공"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        addGradeData(childNode3, "(컴퓨터공학전공)", 2);
        addGradeData(childNode4, "(미디어정보공학전공)", 2);
        parentNode.addChildren(childNode1, childNode2, childNode3, childNode4);
    }

    //예체능 대학
    private void addCollegeOfArtsAndPhysical(TreeNode parentNode) {

        TreeNode childNode1 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark, "회화·디자인학부"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        TreeNode childNode2 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "음악학과"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        TreeNode childNode3 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "사회체육학과"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        addDetailOfArtsAndDesign(childNode1);
        addGradeData(childNode2, "(음악학과)");
        addGradeData(childNode3, "(사회체육학과)");
        parentNode.addChildren(childNode1, childNode2, childNode3);
    }

    //회화 디자인학부
    private void addDetailOfArtsAndDesign(TreeNode parentNode) {
        TreeNode childNode1 = new TreeNode(new DetailViewHolder.DetailViewItem("1학년", "(회화·디자인학부)"))
                .setViewHolder(new DetailViewHolder(getActivity()));

        TreeNode childNode2 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "회화전공"))
                .setViewHolder(new DepartmentHolder(getActivity()));

        TreeNode childNode3 = new TreeNode(new IconTreeItemHolder
                .IconTreeItem(R.string.ic_bookmark_outline, "산업디자인학전공"))
                .setViewHolder(new DepartmentHolder(getActivity()));
        addGradeData(childNode2, "(회화전공)", 1);
        addGradeData(childNode3, "(산업디자인학전공)", 1);
        parentNode.addChildren(childNode1, childNode2, childNode3);
    }


    //교양 과목 표시
    private void addCultureData(TreeNode parentNode) {

        TreeNode cultureChildNode1 = new TreeNode(new DetailViewHolder.DetailViewItem("제 1영역", "(인문학)"))
                .setViewHolder(new DetailViewHolder(getActivity()));
        TreeNode cultureChildNode2 = new TreeNode(new DetailViewHolder.DetailViewItem("제 2영역", "(사회과학)"))
                .setViewHolder(new DetailViewHolder(getActivity()));
        TreeNode cultureChildNode3 = new TreeNode(new DetailViewHolder.DetailViewItem("제 3영역", "(자연과학)"))
                .setViewHolder(new DetailViewHolder(getActivity()));
        TreeNode cultureChildNode4 = new TreeNode(new DetailViewHolder.DetailViewItem("제 4영역", "(생활과예술)"))
                .setViewHolder(new DetailViewHolder(getActivity()));
        TreeNode cultureChildNode5 = new TreeNode(new DetailViewHolder.DetailViewItem("제 5영역", "(리더십과봉사)"))
                .setViewHolder(new DetailViewHolder(getActivity()));
        TreeNode cultureChildNode6 = new TreeNode(new DetailViewHolder.DetailViewItem("제 6영역", "(국제화)"))
                .setViewHolder(new DetailViewHolder(getActivity()));
        parentNode.addChildren(cultureChildNode1, cultureChildNode2,
                cultureChildNode3, cultureChildNode4,
                cultureChildNode5, cultureChildNode6);
    }

    private void addFirstGradeCultureData(TreeNode parentNode) {
        TreeNode firstCultureChildNode = new TreeNode(new DetailViewHolder.DetailViewItem("1학년", "교양필수"))
                .setViewHolder(new DetailViewHolder(getActivity()));
        parentNode.addChildren(firstCultureChildNode);
    }

    //학년 표시
    private void addGradeData(TreeNode parentNode, String deptName) {

        TreeNode gradeChildNode1 = new TreeNode(new DetailViewHolder.DetailViewItem("1학년", deptName))
                .setViewHolder(new DetailViewHolder(getActivity()));
        TreeNode gradeChildNode2 = new TreeNode(new DetailViewHolder.DetailViewItem("2학년", deptName))
                .setViewHolder(new DetailViewHolder(getActivity()));
        TreeNode gradeChildNode3 = new TreeNode(new DetailViewHolder.DetailViewItem("3학년", deptName))
                .setViewHolder(new DetailViewHolder(getActivity()));
        TreeNode gradeChildNode4 = new TreeNode(new DetailViewHolder.DetailViewItem("4학년", deptName))
                .setViewHolder(new DetailViewHolder(getActivity()));

        parentNode.addChildren(gradeChildNode1, gradeChildNode2, gradeChildNode3, gradeChildNode4);
    }

    private void addGradeData(TreeNode parentNode, String majorName, int grade) {

        TreeNode gradeChildNode1 = new TreeNode(new DetailViewHolder.DetailViewItem("2학년", majorName))
                .setViewHolder(new DetailViewHolder(getActivity()));
        TreeNode gradeChildNode2 = new TreeNode(new DetailViewHolder.DetailViewItem("3학년", majorName))
                .setViewHolder(new DetailViewHolder(getActivity()));
        TreeNode gradeChildNode3 = new TreeNode(new DetailViewHolder.DetailViewItem("4학년", majorName))
                .setViewHolder(new DetailViewHolder(getActivity()));

        if (grade == 2) {
            parentNode.addChildren(gradeChildNode2, gradeChildNode3);
        } else {
            parentNode.addChildren(gradeChildNode1, gradeChildNode2, gradeChildNode3);
        }
    }

    private TreeNode.TreeNodeClickListener nodeClickListener = new TreeNode.TreeNodeClickListener() {
        @Override
        public void onClick(TreeNode treeNode, Object value) {
            try {
                if (Integer.parseInt(term.substring(0, 1)) == selectedTerm) {
                    DetailViewHolder.DetailViewItem item = (DetailViewHolder.DetailViewItem) value;
                    Intent intent = new Intent(getActivity(), SubjectActivity.class);
                    intent.putExtra("gradeName", item.gradeName);
                    String dName = item.deptName.replaceAll("[()]", "").trim();
                    intent.putExtra("dName", dName);
                    intent.putExtra("term", selectedTerm);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.in_from_left, R.anim.out_to_left);
                } else {
                    new MaterialDialog.Builder(getContext())
                            .backgroundColor(getResources().getColor(R.color.white))
                            .content("현재 " + selectedTerm + "학기의 수강리뷰를 조회하실 수 없습니다.")
                            .contentColor(getResources().getColor(R.color.text_lgray))
                            .positiveText("확인")
                            .positiveColor(getResources().getColor(R.color.colorPrimary))
                            .show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        selectedTerm = FragmentPagerItem.getPosition(getArguments()) + 1;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tState", tView.getSaveState());
    }
}
