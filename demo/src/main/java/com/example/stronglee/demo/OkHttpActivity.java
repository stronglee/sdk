package com.example.stronglee.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.common.sdk.network.OkHttpUtils;
import com.android.common.sdk.network.callback.StringCallback;
import com.example.stronglee.demo.data.Const;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.squareup.okhttp.Request;

public class OkHttpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        testGetRequest();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            testGetRequest();
            return true;
        }
        if (id == R.id.action_fragment) {
            startContent();
        }

        return super.onOptionsItemSelected(item);
    }

    private void testGetRequest() {
        OkHttpUtils.getInstance().getOkHttpClient().networkInterceptors().add(new StethoInterceptor());
        OkHttpUtils.get().url(Const.BAIDU).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                Toast.makeText(OkHttpActivity.this, response, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void startContent() {
        Intent intent = new Intent(OkHttpActivity.this, ContentActivity.class);
        startActivity(intent);
    }
}
