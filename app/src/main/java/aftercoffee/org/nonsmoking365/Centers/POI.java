package aftercoffee.org.nonsmoking365.Centers;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HYUNWOO on 2015-11-14.
 */
public class POI {
    String id;
    String name;
    String telNo;
    String frontLat;
    String frontLon;
    String noorLat;
    String noorLon;
    String upperAddrName;
    String middleAddrName;
    String lowerAddrName;
    String detailAddrName;
    @SerializedName("radius")
    String distance;

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
