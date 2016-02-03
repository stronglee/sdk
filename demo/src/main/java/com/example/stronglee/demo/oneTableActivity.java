package com.example.stronglee.demo;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stronglee.demo.db.DbManager;
import com.example.stronglee.demo.fragment.AddItemDialog;
import com.example.stronglee.demo.fragment.EditDialog;

import java.util.ArrayList;
import java.util.List;

import greendao.Users;


public class OneTableActivity extends AppCompatActivity implements AddItemDialog.addUserOnClickListener,
        EditDialog.EditUserOnClickListener {
    private Toolbar mToolbar;
    private ListView mListView;

    private List<Users> mListData;
    private TableAdapter mAdapter;

    private DbManager mDbService;

    private EditDialog mDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_table);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle("单表操作");
        setSupportActionBar(mToolbar);
        mDbService = DbManager.getInstance(this);
        initView();
        initData();
    }


    private void initData() {
        mListData = new ArrayList<>();
        mListData = mDbService.loadAllNoteByOrder();
        mAdapter = new TableAdapter(this, mListData);
        mListView.setAdapter(mAdapter);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mListView = (ListView) findViewById(R.id.lv_oneTable);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(OneTableActivity.this, mListData.get(position).getName() + "--" +
                        mListData.get(position).getId(), Toast.LENGTH_SHORT).show();

                mDialogFragment = new EditDialog(mListData.get(position).getId(), position);
                mDialogFragment.show(getFragmentManager(), "编辑");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_one_table, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_onetable_add) {
            AddItemDialog oneDialog = new AddItemDialog(0, "", "", "", "", 0);
            oneDialog.show(getFragmentManager(), "添加用户");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onAddUserOnClick(long uId, String uName, String uSex, String uAge, String uTel, int flag) {
        Users user = new Users();
        if (flag == 1) {
            user.setId(uId);
        }
        user.setSex(uSex);
        user.setPhone(uTel);
        user.setAge(uAge);
        user.setName(uName);
        if (flag == 0) {
            if (mDbService.saveNote(user) > 0) {
                mListData.add(0, user);
                mAdapter.notifyDataSetChanged();
            }
        } else {
            if (mDbService.saveNote(user) > 0) {

                int num = 0;
                for (Users u : mListData) {
                    if (u.getId() == uId) {
                        mListData.remove(num);
                        mListData.add(num, user);

                        break;
                    }
                    num++;
                }
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onEditUserOnClick(long id, int position, int flag) {
        if (flag == 0) {
            mDbService.deleteNote(id);
            mListData.remove(position);
            mAdapter.notifyDataSetChanged();
            mDialogFragment.dismiss();
        } else {
            Users updateUser = mDbService.loadNote(id);
            AddItemDialog oDialog = new AddItemDialog(updateUser.getId(), updateUser.getName(), updateUser.getSex(),
                    updateUser.getAge(), updateUser.getPhone(), 1);
            mDialogFragment.dismiss();
            oDialog.show(getFragmentManager(), "修改");
        }


    }

    public class TableAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private List<Users> mListData;

        public TableAdapter(Context context, List<Users> list) {
            this.inflater = LayoutInflater.from(context);
            this.mListData = list;
        }

        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public Users getItem(int position) {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.activity_onetable_lv_item, null);
                viewHolder.ageView = (TextView) convertView.findViewById(R.id.txt_onetable_age);
                viewHolder.nameView = (TextView) convertView.findViewById(R.id.txt_onetable_uName);
                viewHolder.sexView = (TextView) convertView.findViewById(R.id.txt_onetable_uSex);
                viewHolder.phoneView = (TextView) convertView.findViewById(R.id.txt_onetable_tel);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Users user = getItem(position);
            if (user != null) {
                viewHolder.sexView.setText(user.getSex());
                viewHolder.phoneView.setText(user.getPhone());
                viewHolder.nameView.setText(user.getName());
                viewHolder.ageView.setText(user.getAge());
            }

            return convertView;
        }

        public class ViewHolder {
            TextView nameView;
            TextView sexView;
            TextView ageView;
            TextView phoneView;
        }
    }
}
