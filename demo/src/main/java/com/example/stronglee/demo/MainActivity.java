package com.example.stronglee.demo;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView mListView;
    private ArrayList<ItemData> mDataList;
    private ActivityAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mListView = (ListView) findViewById(R.id.main_list_view);
        mDataList = new ArrayList<>();
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
            if (packageInfo != null&& packageInfo.activities != null) {
                for (ActivityInfo info : packageInfo.activities) {
                    ItemData data = new ItemData();
                    data.name = info.applicationInfo.className;
                    data.explain = info.name;
                    mDataList.add(data);

                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        mAdapter = new ActivityAdapter(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ItemData data = mDataList.get(position);
        if(data != null){
            Intent intent = new Intent();
            intent.setClassName(getPackageName(),data.explain);
            startActivity(intent);
        }
    }

    private class ActivityAdapter extends BaseAdapter {
        private LayoutInflater mLayoutInfalter;

        public ActivityAdapter(Context context) {
            mLayoutInfalter = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            if (mDataList != null) {
                return mDataList.size();
            }
            return 0;
        }

        @Override
        public ItemData getItem(int position) {
            if (position < getCount()) {
                return mDataList.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = mLayoutInfalter.inflate(R.layout.activity_item_layout, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.mTitle = (TextView) convertView.findViewById(R.id.activity_item_title);
                viewHolder.mSubTitle = (TextView) convertView.findViewById(R.id.activity_item_sub);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            ItemData data = getItem(position);
            if (data != null) {
                viewHolder.mTitle.setText(data.name);
                viewHolder.mSubTitle.setText(data.explain);
            }
            return convertView;
        }
    }

    private class ItemData {
        public String name;
        public String explain;
    }

    private class ViewHolder {
        public TextView mTitle;
        public TextView mSubTitle;
    }
}
