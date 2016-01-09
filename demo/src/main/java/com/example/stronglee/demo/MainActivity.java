package com.example.stronglee.demo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
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
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView mListView;
    private ArrayList<ItemData> mDataList;
    private ActivityAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mListView = (ListView) findViewById(R.id.main_list_view);
        mDataList = getListData(Const.QUERY_ACTION);
        mAdapter = new ActivityAdapter(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ItemData data = mDataList.get(position);
        if (data != null) {
            Intent intent = new Intent();
            intent.setClassName(getPackageName(), data.className);
            startActivity(intent);
        }
    }

    private ArrayList<ItemData> getListData(String action) {
        ArrayList<ItemData> result = new ArrayList<>();
        PackageManager packageManager = getPackageManager();
        Intent query = new Intent(action, null);
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(query, 0);
        if (resolveInfoList != null && resolveInfoList.size() > 0) {
            for (ResolveInfo info : resolveInfoList) {
                ItemData data = new ItemData();
                data.className = info.activityInfo.name;
                String[] nameList = data.className.split("\\.");
                data.name = nameList[nameList.length - 1];
                result.add(data);
            }
        }
        return result;
    }

    private class ActivityAdapter extends BaseAdapter {
        private LayoutInflater mLayoutInflater;

        public ActivityAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
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
                convertView = mLayoutInflater.inflate(R.layout.activity_item_layout, parent, false);
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
                //viewHolder.mSubTitle.setText(data.className);
            }
            return convertView;
        }
    }

    private class ItemData {
        public String name;
        public String className;

        @Override
        public String toString() {
            return "ItemData{" +
                    "name='" + name + '\'' +
                    ", className='" + className + '\'' +
                    '}';
        }
    }

    private class ViewHolder {
        public TextView mTitle;
        public TextView mSubTitle;
    }
}
