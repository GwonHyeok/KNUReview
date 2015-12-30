package dev.knureview.Adapter;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import dev.knureview.R;

/**
 * Created by DavidHa on 2015. 12. 30..
 */
public class TicketAdapter extends ArrayAdapter<Integer> {
    private static final int ACCESS = 1;
    private static final int DENIAL = 0;
    private Activity activity;
    private int layout;
    private ArrayList<Integer> ticketList;
    private int talkAuth;
    private TextView myTalkTicket;

    static class ViewHolder {
        private TextView rowText;
        private Button rowBtn;
    }

    public TicketAdapter(Activity activity, int layout, ArrayList<Integer> ticketList) {
        super(activity, layout, ticketList);
        this.activity = activity;
        this.layout = layout;
        this.ticketList = ticketList;
    }

    public void setTalkAuth(int talkAuth) {
        this.talkAuth = talkAuth;
    }

    public void setMyTalkTicket(TextView myTalkTicket){
        this.myTalkTicket = myTalkTicket;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(layout, parent, false);
            vh = new ViewHolder();
            vh.rowText = (TextView) convertView.findViewById(R.id.rowTxt);
            vh.rowBtn = (Button) convertView.findViewById(R.id.rowBtn);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        if (talkAuth == DENIAL && position == 0) {
            vh.rowBtn.setText("사용하기");
        } else {
            vh.rowBtn.setText("선물하기");
        }

        //버튼 이벤트
        vh.rowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vh.rowBtn.getText().toString().equals("사용하기")) {
                    //사용하기 버튼을 눌렀을 경우
                    new UpdateTicket().execute();
                    ticketList.remove(0);
                } else if (vh.rowBtn.getText().toString().equals("선물하기")) {
                    //선물하기 버튼을 눌렀을 경우
                    new SendTicket().execute();
                    ticketList.remove(position);
                }
                notifyDataSetChanged();
            }
        });
        return super.getView(position, convertView, parent);
    }

    /**
     * 사용자가 버튼을 누르고 나서 Dialog 호출
     * 확인을 누르면 notifyDataSetChange 그리고 다른 사용자에게 티켓 전송 or 자기자신한테 적용
    */

    private class UpdateTicket extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... params) {
            try{

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            myTalkTicket.setText("사용중");
        }
    }
    private class SendTicket extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }
    }

}
