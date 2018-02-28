package online.himakeit.baidumapdemo.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import online.himakeit.baidumapdemo.R;

/**
 * @author：LiXueLong
 * @date:2018/2/28-14:08
 * @mail1：skylarklxlong@outlook.com
 * @mail2：li_xuelong@126.com
 * @des：ShareDemo
 */
public class ShareDemo extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_demo);
    }

    public void startShareDemo(View view) {
        Intent intent = new Intent();
        intent.setClass(this, ShareDemoActivity.class);
        startActivity(intent);

    }

}
