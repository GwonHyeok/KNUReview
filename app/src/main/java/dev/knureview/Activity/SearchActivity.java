package dev.knureview.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import dev.knureview.Adapter.SimpleListViewAdapter;
import dev.knureview.R;
import dev.knureview.Util.NetworkUtil;
import dev.knureview.Util.PixelUtil;
import dev.knureview.Util.SharedPreferencesActivity;
import dev.knureview.VO.SubjectVO;

/**
 * Created by DavidHa on 2015. 11. 22..
 */
public class SearchActivity extends ActionBarActivity {

    @Bind(R.id.inputSearch)
    EditText inputSearch;
    @Bind(R.id.totalSubject)
    TextView totalSubject;
    @Bind(R.id.listView)
    ListView listView;
    private SimpleListViewAdapter adapter;
    private ArrayList<String> sbjStrArray;
    private HashMap<String, Integer> sbjHashMap;
    private int reviewAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        //toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //pref
        //pref
        SharedPreferencesActivity pref = new SharedPreferencesActivity(this);
        reviewAuth= pref.getPreferences("reviewAuth",0);
        inputSearch.addTextChangedListener(textWatcher);
        listView.setOnItemClickListener(itemClickListener);
        new FindSubject().execute();
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            setRefreshAdapter(adapter, listView, s.toString());
        }
    };

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(inputSearch.getWindowToken(), 0);
            String sbjName = adapter.getItemList().get(position);
            inputSearch.setText(sbjName);
            inputSearch.setSelection(inputSearch.getText().length());
            if(reviewAuth == 1) {
                Intent intent = new Intent(SearchActivity.this, ReviewActivity.class);
                intent.putExtra("sNo", sbjHashMap.get(sbjName));
                intent.putExtra("sName", sbjName);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_left);
            }else{
                new MaterialDialog.Builder(SearchActivity.this)
                        .title("수강리뷰 열람 불가능")
                        .titleColor(getResources().getColor(R.color.black))
                        .backgroundColor(getResources().getColor(R.color.white))
                        .content("수강리뷰를 볼 수 있는 권한이 없습니다.\n수강리뷰를 5개 이상 등록하셔야만 수강리뷰를 볼 수 있습니다.")
                        .contentColor(getResources().getColor(R.color.text_lgray))
                        .positiveText("확인")
                        .positiveColor(getResources().getColor(R.color.colorPrimary))
                        .cancelable(false)
                        .iconRes(R.drawable.not_verified_ic)
                        .maxIconSize(96)
                        .show();
            }
        }
    };

    public void setRefreshAdapter(SimpleListViewAdapter adapter, ListView listView, String str) {
        adapter.autoComplete(str);
        totalSubject.setText("총 " + adapter.getCount() + "개의 과목이 검색 되었습니다.");
        adapter.notifyDataSetChanged();
    }

    private class FindSubject extends AsyncTask<Void, Void, ArrayList<SubjectVO>> {
        @Override
        protected ArrayList<SubjectVO> doInBackground(Void... params) {
            try {
                return new NetworkUtil().getAllSubjectList();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<SubjectVO> result) {
            super.onPostExecute(result);
            sbjStrArray = new ArrayList<>();
            sbjHashMap = new HashMap<>();
            for (int i = 0; i < result.size(); i++) {
                sbjStrArray.add(result.get(i).getsName());
                sbjHashMap.put(result.get(i).getsName(), result.get(i).getsNo());
            }
            adapter = new SimpleListViewAdapter(SearchActivity.this, R.layout.layout_rv_sbj_list_row, sbjStrArray);
            listView.setAdapter(adapter);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_right);
    }
}
