package aftercoffee.org.nonsmoking365.Centers;

/**
 * Created by HYUNWOO on 2015-11-14.
 */
public class POI {
    //String id;
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

    @Override
    public String toString() {
        return name;
    }

    public double getLatitude() {
        return (Double.parseDouble(frontLat) + Double.parseDouble(noorLat)) / 2;
    }

    public double getLongitude() {
        return (Double.parseDouble(frontLon) + Double.parseDouble(noorLon)) / 2;
    }

    public String getAddress() {
        return upperAddrName + " " + middleAddrName + " " + lowerAddrName + " " + detailAddrName;
    }

    public double getLatitudeL1() {
        return 0;
    }
}
