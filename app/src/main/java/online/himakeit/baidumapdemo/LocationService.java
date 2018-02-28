package online.himakeit.baidumapdemo;

import android.content.Context;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

/**
 * @author：LiXueLong
 * @date:2018/2/28-16:38
 * @mail1：skylarklxlong@outlook.com
 * @mail2：li_xuelong@126.com
 * @des：LocationService
 */
public class LocationService {
    private LocationClient client = null;
    public LocationClientOption mOption, DIYoption;
    private Object objLock = new Object();

    /***
     *
     * @param locationContext
     */
    public LocationService(Context locationContext) {
        synchronized (objLock) {
            if (client == null) {
                client = new LocationClient(locationContext);
                client.setLocOption(getDefaultLocationClientOption());
            }
        }
    }

    /***
     *
     * @param listener
     * @return
     */

    public boolean registerListener(BDLocationListener listener) {
        boolean isSuccess = false;
        if (listener != null) {
            client.registerLocationListener(listener);
            isSuccess = true;
        }
        return isSuccess;
    }

    public void unregisterListener(BDLocationListener listener) {
        if (listener != null) {
            client.unRegisterLocationListener(listener);
        }
    }

    /***
     *
     * @param option
     * @return isSuccessSetOption
     */
    public boolean setLocationOption(LocationClientOption option) {
        boolean isSuccess = false;
        if (option != null) {
            if (client.isStarted()){
                client.stop();
            }
            DIYoption = option;
            client.setLocOption(option);
        }
        return isSuccess;
    }

    public LocationClientOption getOption() {
        return DIYoption;
    }

    /***
     *
     * @return DefaultLocationClientOption
     */
    public LocationClientOption getDefaultLocationClientOption() {
        if (mOption == null) {
            mOption = new LocationClientOption();
            /**
             * 可选，默认高精度，设置定位模式
             * 高精度 LocationMode.Hight_Accuracy
             * 低功耗 LocationMode. Battery_Saving
             * 仅使用设备 LocationMode. Device_Sensors
             */
            mOption.setLocationMode(LocationMode.Hight_Accuracy);
            /**
             * 可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
             * gcj02：国测局坐标；
             * bd09ll：百度经纬度坐标；
             * bd09：百度墨卡托坐标；
             * 海外地区定位，无需设置坐标类型，统一返回wgs84类型坐标
             */
            mOption.setCoorType("bd09ll");
            /**
             * 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
             */
            mOption.setScanSpan(3000);
            /**
             * 可选，设置是否使用gps，默认false
             * 使用高精度和仅用设备两种定位模式的，参数必须设置为true
             */
            mOption.setOpenGps(true);
            /**
             * 可选，设置是否需要地址信息，默认不需要
             */
            mOption.setIsNeedAddress(true);
            /**
             * 可选，设置是否需要地址描述
             */
            mOption.setIsNeedLocationDescribe(true);
            /**
             * 可选，设置是否需要设备方向结果
             */
            mOption.setNeedDeviceDirect(false);
            /**
             * 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
             */
            mOption.setLocationNotify(false);
            /**
             * 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
             */
            mOption.setIgnoreKillProcess(true);
            /**
             * 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
             */
            mOption.setIsNeedLocationDescribe(true);
            /**
             * 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
             */
            mOption.setIsNeedLocationPoiList(true);
            /**
             * 可选，默认false，设置是否收集CRASH信息，默认收集
             */
            mOption.SetIgnoreCacheException(false);
            /**
             * 可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
             */
            mOption.setIsNeedAltitude(false);
            /**
             * 可选，7.2版本新增能力
             * 如果设置了该接口，首次启动定位时，会先判断当前WiFi是否超出有效期，若超出有效期，会先重新扫描WiFi，然后定位
             */
            mOption.setWifiCacheTimeOut(5*60*1000);
            /**
             * 可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false
             */
            mOption.setEnableSimulateGps(false);
        }
        return mOption;
    }

    public void start() {
        synchronized (objLock) {
            if (client != null && !client.isStarted()) {
                client.start();
            }
        }
    }

    public void stop() {
        synchronized (objLock) {
            if (client != null && client.isStarted()) {
                client.stop();
            }
        }
    }

    public boolean requestHotSpotState() {

        return client.requestHotSpotState();

    }

}
