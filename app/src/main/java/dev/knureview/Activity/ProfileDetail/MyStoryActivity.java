package dev.knureview.Activity.ProfileDetail;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import dev.knureview.Activity.StDetailActivity;
import dev.knureview.Adapter.StoryAdapter;
import dev.knureview.R;
import dev.knureview.Util.NetworkUtil;
import dev.knureview.Util.SharedPreferencesActivity;
import dev.knureview.VO.TalkVO;

/**
 * Created by DavidHa on 2015. 12. 26..
 */
public class MyStoryActivity extends ActionBarActivity {

    private ListView listView;
    private TextView myTotalStory;
    private String stdNo;
    private StoryAdapter adapter;
    private ArrayList<TalkVO> talkList;
    private int listPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_story);

        listView = (ListView)findViewById(R.id.listView);
        myTotalStory =(TextView)findViewById(R.id.myTotalStory);
        listView.setOnItemClickListener(listItemListener);

        //pref
        SharedPreferencesActivity pref = new SharedPreferencesActivity(this);
        stdNo = pref.getPreferences("stdNo","");
        new MyTalkList().execute();

    }

    AdapterView.OnItemClickListener listItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            listPosition = position;
            Intent intent = new Intent(MyStoryActivity.this, StDetailActivity.class);
            intent.putExtra("tNo", talkList.get(position).gettNo());
            intent.putExtra("pictureURL", talkList.get(position).getPictureURL());
            intent.putExtra("stdNo", talkList.get(position).getStdNo());
            intent.putExtra("description", talkList.get(position).getDescription());
            intent.putExtra("writeTime", talkList.get(position).getWriteTime());
            intent.putExtra("likeCnt", talkList.get(position).getLikeCnt());
            intent.putExtra("commentCnt", talkList.get(position).getCommentCnt());
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_left, R.anim.out_to_left);
        }
    };


    private class MyTalkList extends AsyncTask<Void, Void, ArrayList<TalkVO>>{
        @Override
        protected ArrayList<TalkVO> doInBackground(Void... params) {
            try{
                return new NetworkUtil().getMyTalkList(stdNo);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<TalkVO> data) {
            super.onPostExecute(data);
            talkList = data;
            myTotalStory.setText("총 " + data.size()+"개의 이야기가 있습니다.");
            adapter = new StoryAdapter(MyStoryActivity.this, R.layout.layout_story_list_row, data);
            adapter.showDate();
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            listView.setSelection(listPosition);
        }
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_right);
    }
}
