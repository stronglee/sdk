package com.example.stronglee.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.stronglee.demo.db.DbService;

import java.util.ArrayList;
import java.util.List;


public class oneTableActivity extends AppCompatActivity implements com.example.cg.greendaolearn.oneTableDialogFragment.addUserOnClickListener, com.example.cg.greendaolearn.oneTableItemDialogFragment.EditUserOnClickListener {

    private Toolbar toolbar;                                                   //定义toolbar
    private ListView lv_oneTable;

    private List<Users> list_user;
    private com.example.cg.greendaolearn.adpater.onetable_adapter oAdapter;

    private DbService db;

    private com.example.cg.greendaolearn.oneTableItemDialogFragment oneItemDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_table);

        toolbar = (Toolbar)this.findViewById(R.id.toolbar);
        toolbar.setTitle("单表操作");                     // 标题的文字需在setSupportActionBar之前，不然会无效
        setSupportActionBar(toolbar);

        db = DbService.getInstance(this);

        initControls();

        initData();
    }

    /**
     * 初始化数据,刚进入页面时加载
     */
    private void initData() {

        list_user = new ArrayList<>();
        list_user = db.loadAllNoteByOrder();
        oAdapter = new com.example.cg.greendaolearn.adpater.onetable_adapter(this,list_user);
        lv_oneTable.setAdapter(oAdapter);
    }

    /**
     * 初始化控件
     */
    private void initControls() {

        lv_oneTable = (ListView)findViewById(R.id.lv_oneTable);
        lv_oneTable.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(oneTableActivity.this,list_user.get(position).getUName() + "--" +
                        list_user.get(position).getId(),Toast.LENGTH_SHORT).show();

                oneItemDialog = new com.example.cg.greendaolearn.oneTableItemDialogFragment(list_user.get(position).getId(),position);
                oneItemDialog.show(getFragmentManager(),"编辑");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
            com.example.cg.greendaolearn.oneTableDialogFragment oneDialog = new com.example.cg.greendaolearn.oneTableDialogFragment(0,"","","","",0);
            oneDialog.show(getFragmentManager(),"添加用户");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onAddUserOnClick(long uId,String uName, String uSex, String uAge, String uTel,int flag) {
        Users user = new Users();
        if(flag==1) {
            user.setId(uId);
        }
        user.setUSex(uSex);
        user.setUTelphone(uTel);
        user.setUAge(uAge);
        user.setUName(uName);
        if(flag==0) {
            if (db.saveNote(user) > 0) {
                list_user.add(0, user);
                oAdapter.notifyDataSetChanged();
            }
        }else
        {
            if (db.saveNote(user) > 0) {

                int num = 0;
                for(Users u:list_user)
                {
                    if(u.getId()==uId)
                    {
                        list_user.remove(num);
                        list_user.add(num,user);

                        break;
                    }
                    num++;
                }
                oAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onEditUserOnClick(long id,int postion,int flag) {
        if(flag==0) {
            db.deleteNote(id);
            list_user.remove(postion);
            oAdapter.notifyDataSetChanged();
            oneItemDialog.dismiss();
        }else
        {
            Users updateUser = db.loadNote(id);
            com.example.cg.greendaolearn.oneTableDialogFragment oDialog = new com.example.cg.greendaolearn.oneTableDialogFragment(updateUser.getId(),updateUser.getUName(),updateUser.getUSex(),
                    updateUser.getUAge(), updateUser.getUTelphone(),1);
            oneItemDialog.dismiss();
            oDialog.show(getFragmentManager(),"修改");
        }


    }
}
