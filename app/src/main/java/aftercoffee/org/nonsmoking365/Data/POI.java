package aftercoffee.org.nonsmoking365.Data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HYUNWOO on 2015-11-14.
 */
public class POI {
    public String id;
    public String name;
    public String telNo;
    public String frontLat;
    public String frontLon;
    public String noorLat;
    public String noorLon;
    public String upperAddrName;
    public String middleAddrName;
    public String lowerAddrName;
    public String detailAddrName;
    @SerializedName("radius")
    public String distance;

    @Override
    public String toString() {
        return name;
    }

    public String getDistance() {
        return distance;
    }

    public double getLatitude() {
        return Double.parseDouble(frontLat);
    }

    public double getLongitude() {
        return Double.parseDouble(frontLon);
    }

    public String getAddress() {
        return upperAddrName + " " + middleAddrName + " " + lowerAddrName + " " + detailAddrName;
    }

}
