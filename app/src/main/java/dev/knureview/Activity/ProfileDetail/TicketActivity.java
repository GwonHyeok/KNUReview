package dev.knureview.Activity.ProfileDetail;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import dev.knureview.Adapter.TicketAdapter;
import dev.knureview.R;
import dev.knureview.Util.NetworkUtil;
import dev.knureview.Util.SharedPreferencesActivity;
import dev.knureview.VO.StudentVO;

/**
 * Created by DavidHa on 2015. 12. 28..
 */
public class TicketActivity extends ActionBarActivity {
    private String stdNo;
    private TextView totalTicketTxt;
    private TextView myTalkTicket;
    private TextView alarmNoTicketTxt;
    private ListView listView;
    private TicketAdapter adapter;
    private EditText stdNoInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        //toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        totalTicketTxt = (TextView) findViewById(R.id.totalTicket);
        myTalkTicket = (TextView) findViewById(R.id.myTalkTicket);
        listView = (ListView) findViewById(R.id.listView);
        alarmNoTicketTxt = (TextView) findViewById(R.id.alarmNoTicket);

        //pref
        SharedPreferencesActivity pref = new SharedPreferencesActivity(this);
        stdNo = pref.getPreferences("stdNo", "");

        new MemberInfo().execute();
    }


    public void mOnClick(View view) {
        if (view.getId() == R.id.rowBtn) {
            Button rowBtn = (Button) view;
            //선물하기 버튼을 눌렀을 경우
            if (rowBtn.getText().toString().equals("선물하기")) {
                MaterialDialog dialog = new MaterialDialog.Builder(this)
                        .backgroundColor(getResources().getColor(R.color.white))
                        .title("선물하기")
                        .titleColor(getResources().getColor(R.color.black))
                        .customView(R.layout.layout_ticket_gift_dialog, true)
                        .positiveText("확인")
                        .positiveColor(getResources().getColor(R.color.colorPrimary))
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                                String giftStdNo = stdNoInput.getText().toString();
                                if (giftStdNo.length() == 9) {
                                    new FindMember().execute(giftStdNo);
                                }else if(giftStdNo.equals(stdNo)){
                                    Toast.makeText(TicketActivity.this,"본인한테는 선물이 불가능합니다."
                                            ,Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(TicketActivity.this, "학번을 정확히 입력해 주십시오."
                                            , Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .negativeText("취소")
                        .negativeColor(getResources().getColor(R.color.colorPrimary))
                        .iconRes(R.drawable.gift_ic)
                        .maxIconSize(90)
                        .show();
                stdNoInput = (EditText) dialog.getCustomView().findViewById(R.id.stdNoInput);
            } else {
                //사용하기 버튼을 눌렀을 경우
                new MaterialDialog.Builder(TicketActivity.this)
                        .backgroundColor(getResources().getColor(R.color.white))
                        .title("사용하기")
                        .titleColor(getResources().getColor(R.color.black))
                        .content("소곤소곤 티켓을 사용하시겠습니까?")
                        .contentColor(getResources().getColor(R.color.text_lgray))
                        .positiveText("확인")
                        .positiveColor(getResources().getColor(R.color.colorPrimary))
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                                new UpdateTicketAuth().execute();
                            }
                        })
                        .negativeText("취소")
                        .negativeColor(getResources().getColor(R.color.colorPrimary))
                        .iconRes(R.drawable.delete_ticket_ic)
                        .maxIconSize(90)
                        .show();
            }
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

    private class MemberInfo extends AsyncTask<Void, Void, StudentVO> {
        @Override
        protected StudentVO doInBackground(Void... params) {
            try {
                return new NetworkUtil().getExistMemberInfo(stdNo);
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(StudentVO result) {
            super.onPostExecute(result);
            int totalTicket = result.getTalkTicket();
            int talkAuth = result.getTalkAuth();
            totalTicketTxt.setText(String.valueOf(totalTicket));
            if (talkAuth == 1) {
                myTalkTicket.setText("사용중");
            } else {
                myTalkTicket.setText("없음");
            }
            if ((totalTicket > 1 && talkAuth == 1) || talkAuth == 0 && totalTicket > 0) {
                alarmNoTicketTxt.setVisibility(View.GONE);
                adapter = new TicketAdapter(TicketActivity.this, R.layout.layout_ticket_list_row,
                        talkAuth, totalTicket - talkAuth);
                listView.setAdapter(adapter);
                listView.setVisibility(View.VISIBLE);
            } else {
                alarmNoTicketTxt.setVisibility(View.VISIBLE);
                listView.setVisibility(View.INVISIBLE);
            }
        }
    }

    private class FindMember extends AsyncTask<String, Void, StudentVO> {

        @Override
        protected StudentVO doInBackground(String... params) {
            try {
                return new NetworkUtil().getExistMemberInfo(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(StudentVO result) {
            super.onPostExecute(result);
            if (result.isExist()) {
                new SendTicket().execute(String.valueOf(result.getStdNo()));
            } else {
                new MaterialDialog.Builder(TicketActivity.this)
                        .backgroundColor(getResources().getColor(R.color.white))
                        .content("존재하지 않은 회원입니다.")
                        .contentColor(getResources().getColor(R.color.text_lgray))
                        .positiveText("확인")
                        .positiveColor(getResources().getColor(R.color.colorPrimary))
                        .show();
            }
        }
    }

    private class SendTicket extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            try {
                new NetworkUtil().sendMemberTicket(stdNo, params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new MemberInfo().execute();
            new MaterialDialog.Builder(TicketActivity.this)
                    .backgroundColor(getResources().getColor(R.color.white))
                    .content("티켓을 성공적으로 보냈습니다.")
                    .contentColor(getResources().getColor(R.color.text_lgray))
                    .positiveText("확인")
                    .positiveColor(getResources().getColor(R.color.colorPrimary))
                    .show();
        }
    }
    private class UpdateTicketAuth extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... params) {
            try{
                new NetworkUtil().updateMemberTicketAuth(stdNo);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new MemberInfo().execute();
            new MaterialDialog.Builder(TicketActivity.this)
                    .backgroundColor(getResources().getColor(R.color.white))
                    .content("지금부터 소곤소곤에 글을 남기실 수 있습니다")
                    .contentColor(getResources().getColor(R.color.text_lgray))
                    .positiveText("확인")
                    .positiveColor(getResources().getColor(R.color.colorPrimary))
                    .cancelable(false)
                    .show();
        }
    }
}
