/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package online.himakeit.baidumapdemo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.VersionInfo;

import online.himakeit.baidumapdemo.cloud.CloudSearchDemo;
import online.himakeit.baidumapdemo.map.BaseMapDemo;
import online.himakeit.baidumapdemo.map.FavoriteDemo;
import online.himakeit.baidumapdemo.map.GeometryDemo;
import online.himakeit.baidumapdemo.map.HeatMapDemo;
import online.himakeit.baidumapdemo.map.IndoorMapDemo;
import online.himakeit.baidumapdemo.map.LayersDemo;
import online.himakeit.baidumapdemo.map.LocationDemo;
import online.himakeit.baidumapdemo.map.MapControlDemo;
import online.himakeit.baidumapdemo.map.MapFragmentDemo;
import online.himakeit.baidumapdemo.map.MarkerClusterDemo;
import online.himakeit.baidumapdemo.map.MultiMapViewDemo;
import online.himakeit.baidumapdemo.map.OfflineDemo;
import online.himakeit.baidumapdemo.map.OpenglDemo;
import online.himakeit.baidumapdemo.map.OverlayDemo;
import online.himakeit.baidumapdemo.map.TextureMapViewDemo;
import online.himakeit.baidumapdemo.map.TileOverlayDemo;
import online.himakeit.baidumapdemo.map.TrackShowDemo;
import online.himakeit.baidumapdemo.map.UISettingDemo;
import online.himakeit.baidumapdemo.radar.RadarDemo;
import online.himakeit.baidumapdemo.search.BusLineSearchDemo;
import online.himakeit.baidumapdemo.search.DistrictSearchDemo;
import online.himakeit.baidumapdemo.search.GeoCoderDemo;
import online.himakeit.baidumapdemo.search.IndoorSearchDemo;
import online.himakeit.baidumapdemo.search.PoiSearchDemo;
import online.himakeit.baidumapdemo.search.RoutePlanDemo;
import online.himakeit.baidumapdemo.search.ShareDemo;
import online.himakeit.baidumapdemo.util.OpenBaiduMap;

/**
 * @author：LiXueLong
 * @date:2018/2/28-14:09
 * @mail1：skylarklxlong@outlook.com
 * @mail2：li_xuelong@126.com
 * @des：BMapApiDemoMain
 */
public class BMapApiDemoMain extends AppCompatActivity {
    private static final String LTAG = BMapApiDemoMain.class.getSimpleName();

    /**
     * 构造广播监听类，监听 SDK key 验证以及网络异常广播
     */
    public class SDKReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();
            Log.d(LTAG, "action: " + s);
            TextView text = (TextView) findViewById(R.id.text_Info);
            text.setTextColor(Color.RED);
            if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
                text.setText("key 验证出错! 错误码 :" + intent.getIntExtra
                        (SDKInitializer.SDK_BROADTCAST_INTENT_EXTRA_INFO_KEY_ERROR_CODE, 0)
                        + " ; 请在 AndroidManifest.xml 文件中检查 key 设置");
            } else if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK)) {
                text.setText("key 验证成功! 功能可以正常使用");
                text.setTextColor(Color.YELLOW);
            } else if (s.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
                text.setText("网络出错");
            }
        }
    }

    private SDKReceiver mReceiver;

    public static LocationService locationService;
    public static String locationStr;
    public static double latitude;     // 纬度
    public static double lontitude;    // 经度
    public static String city;
    public static String province;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        TextView text = (TextView) findViewById(R.id.text_Info);
        text.setTextColor(Color.YELLOW);
        text.setText("欢迎使用百度地图Android SDK v" + VersionInfo.getApiVersion());
        setTitle(getTitle() + " v" + VersionInfo.getApiVersion());
        ListView mListView = (ListView) findViewById(R.id.listView);
        // 添加ListItem，设置事件响应
        mListView.setAdapter(new DemoListAdapter());
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int index,
                                    long arg3) {
                onListItemClick(index);
            }
        });

        // 注册 SDK 广播监听者
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK);
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        mReceiver = new SDKReceiver();
        registerReceiver(mReceiver, iFilter);
    }

    void onListItemClick(int index) {
        Intent intent;
        intent = new Intent(BMapApiDemoMain.this, DEMOS[index].demoClass);
        this.startActivity(intent);
    }

    private static final DemoInfo[] DEMOS = {
            new DemoInfo(R.string.demo_title_basemap,
                    R.string.demo_desc_basemap, BaseMapDemo.class),
            new DemoInfo(R.string.demo_title_map_fragment,
                    R.string.demo_desc_map_fragment, MapFragmentDemo.class),
            new DemoInfo(R.string.demo_title_layers, R.string.demo_desc_layers,
                    LayersDemo.class),
            new DemoInfo(R.string.demo_title_multimap,
                    R.string.demo_desc_multimap, MultiMapViewDemo.class),
            new DemoInfo(R.string.demo_title_control,
                    R.string.demo_desc_control, MapControlDemo.class),
            new DemoInfo(R.string.demo_title_ui,
                    R.string.demo_desc_ui, UISettingDemo.class),
            new DemoInfo(R.string.demo_title_location,
                    R.string.demo_desc_location, LocationDemo.class),
            new DemoInfo(R.string.demo_title_geometry,
                    R.string.demo_desc_geometry, GeometryDemo.class),
            new DemoInfo(R.string.demo_title_overlay,
                    R.string.demo_desc_overlay, OverlayDemo.class),
            new DemoInfo(R.string.demo_title_heatmap,
                    R.string.demo_desc_heatmap, HeatMapDemo.class),
            new DemoInfo(R.string.demo_title_geocode,
                    R.string.demo_desc_geocode, GeoCoderDemo.class),
            new DemoInfo(R.string.demo_title_poi,
                    R.string.demo_desc_poi, PoiSearchDemo.class),
            new DemoInfo(R.string.demo_title_route,
                    R.string.demo_desc_route, RoutePlanDemo.class),
            new DemoInfo(R.string.demo_title_districsearch,
                    R.string.demo_desc_districsearch, DistrictSearchDemo.class),
            new DemoInfo(R.string.demo_title_bus,
                    R.string.demo_desc_bus, BusLineSearchDemo.class),
            new DemoInfo(R.string.demo_title_share,
                    R.string.demo_desc_share, ShareDemo.class),
            new DemoInfo(R.string.demo_title_offline,
                    R.string.demo_desc_offline, OfflineDemo.class),
            new DemoInfo(R.string.demo_title_radar,
                    R.string.demo_desc_radar, RadarDemo.class),
            new DemoInfo(R.string.demo_title_open_baidumap,
                    R.string.demo_desc_open_baidumap, OpenBaiduMap.class),
            new DemoInfo(R.string.demo_title_favorite,
                    R.string.demo_desc_favorite, FavoriteDemo.class),
            new DemoInfo(R.string.demo_title_cloud,
                    R.string.demo_desc_cloud, CloudSearchDemo.class),
            new DemoInfo(R.string.demo_title_opengl,
                    R.string.demo_desc_opengl, OpenglDemo.class),
            new DemoInfo(R.string.demo_title_cluster,
                    R.string.demo_desc_cluster, MarkerClusterDemo.class),
            new DemoInfo(R.string.demo_title_tileoverlay,
                    R.string.demo_desc_tileoverlay, TileOverlayDemo.class),
            new DemoInfo(R.string.demo_desc_texturemapview,
                    R.string.demo_desc_texturemapview, TextureMapViewDemo.class),
            new DemoInfo(R.string.demo_title_indoor,
                    R.string.demo_desc_indoor, IndoorMapDemo.class),
            new DemoInfo(R.string.demo_title_indoorsearch,
                    R.string.demo_desc_indoorsearch, IndoorSearchDemo.class),
            new DemoInfo(R.string.demo_track_show,
                    R.string.demo_desc_track_show, TrackShowDemo.class)
    };

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();

        locationService = new LocationService(getApplicationContext());
        locationService.registerListener(mListener);
        locationService.registerNotify(notifyListener);
        notifyListener.SetNotifyLocation(30.429502f, 114.469785f, 3000, locationService.getDefaultLocationClientOption().getCoorType());
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();// 定位SDK
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 取消监听 SDK 广播
        unregisterReceiver(mReceiver);
    }

    /**
     * 位置提醒
     */
    private BDNotifyListener notifyListener = new BDNotifyListener() {
        @Override
        public void onNotify(BDLocation bdLocation, float v) {
            Log.e(LTAG, "到达目的地：武汉市江夏区富五路");
            Toast.makeText(BMapApiDemoMain.this, "到达目的地：武汉市江夏区富五路", Toast.LENGTH_SHORT).show();
            locationService.removeNotifyEvent(notifyListener);
        }
    };

    /**
     * 监听位置
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuffer sb = new StringBuffer(256);

                sb.append("latitude : ");// 纬度
                sb.append(location.getLatitude());
                latitude = location.getLatitude();

                sb.append("\nlontitude : ");// 经度
                sb.append(location.getLongitude());
                lontitude = location.getLongitude();

                sb.append("\nCity : ");   //所在城市
                sb.append(location.getCity());
                city = location.getCity();
                province = location.getProvince();

                sb.append("\naddr : ");// 地址信息
                sb.append(location.getAddrStr());
                locationStr = sb.toString();

                Toast.makeText(BMapApiDemoMain.this, locationStr, Toast.LENGTH_SHORT).show();
                Log.e(LTAG, "locationStr" + locationStr);

                locationService.unregisterListener(mListener); //注销掉监听
                locationService.stop(); //停止定位服务
            }
        }

    };

    private class DemoListAdapter extends BaseAdapter {
        public DemoListAdapter() {
            super();
        }

        @Override
        public View getView(int index, View convertView, ViewGroup parent) {
            convertView = View.inflate(BMapApiDemoMain.this,
                    R.layout.demo_info_item, null);
            TextView title = (TextView) convertView.findViewById(R.id.title);
            TextView desc = (TextView) convertView.findViewById(R.id.desc);
            title.setText(DEMOS[index].title);
            desc.setText(DEMOS[index].desc);
            if (index >= 25) {
                title.setTextColor(Color.YELLOW);
            }
            return convertView;
        }

        @Override
        public int getCount() {
            return DEMOS.length;
        }

        @Override
        public Object getItem(int index) {
            return DEMOS[index];
        }

        @Override
        public long getItemId(int id) {
            return id;
        }
    }

    private static class DemoInfo {
        private final int title;
        private final int desc;
        private final Class<? extends Activity> demoClass;

        public DemoInfo(int title, int desc,
                        Class<? extends Activity> demoClass) {
            this.title = title;
            this.desc = desc;
            this.demoClass = demoClass;
        }
    }
}