package online.himakeit.baidumapdemo.cloud;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import online.himakeit.baidumapdemo.R;

/**
 * @author：LiXueLong
 * @date:2018/2/28-14:06
 * @mail1：skylarklxlong@outlook.com
 * @mail2：li_xuelong@126.com
 * @des：CloudSearchDemo
 */
public class CloudSearchDemo extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_search_demo);
    }

    public void startCloudSearchDemo(View view) {
        Intent intent = new Intent();
        intent.setClass(this, CloudSearchActivity.class);
        startActivity(intent);

    }
}
