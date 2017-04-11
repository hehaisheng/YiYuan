package com.shawnway.nav.app.yylg.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.bean.MyBuyRecordWrapper.MyBuyRecordBean.DrawDetail;
import com.shawnway.nav.app.yylg.bean.MyBuyRecordWrapper.*;
import com.shawnway.nav.app.yylg.view.CirclerImageView;

import java.util.ArrayList;

/**
 * Created by Eiffel on 2016/3/29.
 */
public class CodeListAdapter extends MCyclerAdapter<DrawDetail> {
    private final int TYPE_HEADER = 0;
    private final int TYPE_ITEM = 1;
    private int headerData;
    ArrayList<String> lvData = new ArrayList<>();
    private Context mContext;

    public CodeListAdapter(Context context) {
        this(new ArrayList<DrawDetail>(), context);
        mContext = context;
    }

    public CodeListAdapter(ArrayList<DrawDetail> list, Context context) {
        super(list, context);
        mContext = context;
    }

    @Override
    public int getDisplayType(int postion) {
        if (postion == 0)
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateDisplayHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                HeaderHolder holder = new HeaderHolder(inflater.inflate(R.layout.layout_codelist_frag_header, parent, false));
                holder.amount.setText(String.valueOf(headerData));
                return holder;
            default:
                return new ItemHolder(inflater.inflate(R.layout.layout_item_buyedcode, parent, false));
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, int position) {
        switch (getDisplayType(position)) {
            case TYPE_HEADER:
                HeaderHolder headerHolder = (HeaderHolder) holder;
                headerHolder.amount.setText(String.valueOf(headerData));
                break;
            case TYPE_ITEM:
                ItemHolder _holder = (ItemHolder) holder;
                DrawDetail data = list.get(position - 1);
                //2016-09-22 11:38:20 000购买了7份 -->2016-09-22 11:38:20  参与了7人次
                String purchSummary = data.purchSummary;
                String millis = purchSummary.substring(20,23);
                purchSummary = purchSummary.replace("购买了","参与了").replace("份","人次").replace(millis," ");
                _holder.date.setText(purchSummary);
                _holder.code.setText(data.drawNumbers);
                String[] code = data.drawNumbers.split(" ");
                for(int i = 0;i<code.length;i++){
                    if(i%5==0){
                        lvData.add(code[i]+" ");
                        if(i+1<code.length){
                            lvData.add(code[i+1]+" ");
                        }
                        if(i+2<code.length){
                            lvData.add(code[i+2]+" ");
                        }
                        if(i+3<code.length){
                            lvData.add(code[i+3]+" ");
                        }
                        if(i+4<code.length){
                            lvData.add(code[i+4]+" ");
                        }
                    }else{
                        continue;
                    }
                }
                break;
        }

    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager(Context context) {
        LinearLayoutManager lm = new LinearLayoutManager(context);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        return lm;
    }

    public void setHeaderData(int headerData) {
        this.headerData = headerData;
    }

    public int getHeaderData() {
        return headerData;
    }

    class HeaderHolder extends RecyclerView.ViewHolder {
        TextView amount;


        public HeaderHolder(View itemView) {
            super(itemView);
            amount = (TextView) itemView.findViewById(R.id.count);
        }
    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView code;

        public ItemHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.text);
            code = (TextView) view.findViewById(R.id.text1);
        }
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return lvData.size();
        }

        @Override
        public Object getItem(int position) {
            return lvData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView==null){
                convertView= View.inflate(mContext,R.layout.item_buyedcode_lv,parent);
                holder=new ViewHolder();
                holder.code1 = (TextView) convertView.findViewById(R.id.code_text1);
                holder.code2 = (TextView) convertView.findViewById(R.id.code_text2);
                holder.code3 = (TextView) convertView.findViewById(R.id.code_text3);
                holder.code4 = (TextView) convertView.findViewById(R.id.code_text4);
                holder.code5 = (TextView) convertView.findViewById(R.id.code_text5);
                convertView.setTag(holder);
            }else {
                holder=(ViewHolder)convertView.getTag();
            }
            String item = (String) getItem(position);
            String[] code = item.split(" ");
            switch (code.length){
                case 0:
                    break;
                case 1:
                    holder.code1.setText(code[0]);
                    break;
                case 2:
                    holder.code1.setText(code[0]);
                    holder.code2.setText(code[1]);
                    break;
                case 3:
                    holder.code1.setText(code[0]);
                    holder.code2.setText(code[1]);
                    holder.code3.setText(code[2]);
                    break;
                case 4:
                    holder.code1.setText(code[0]);
                    holder.code2.setText(code[1]);
                    holder.code3.setText(code[2]);
                    holder.code4.setText(code[3]);
                    break;
                case 5:
                    holder.code1.setText(code[0]);
                    holder.code2.setText(code[1]);
                    holder.code3.setText(code[2]);
                    holder.code4.setText(code[3]);
                    holder.code5.setText(code[4]);
                    break;
                default:
                    break;
            }
            return convertView;
        }
    }

    class ViewHolder{
        TextView code1;
        TextView code2;
        TextView code3;
        TextView code4;
        TextView code5;
    }
}
