package com.wifi.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WifiData {
    private String id;
    private String district;
    private String wifi_name;
    private String road_address;
    private String detailed_address;
    private String install_floor;
    private String install_type;
    private String install_agency;
    private String service_type;
    private String network_type;
    private int install_year;
    private String indoor_outdoor;
    private String wifi_env;
    private double lat;
    private double lng;
    private String work_time;

    // 기본 생성자
    public WifiData() {}

    // 전체 필드를 포함 생성자
    public WifiData(String id, String district, String wifiName, String roadAddress, String detailedAddress, String installFloor, 
            String installType, String installAgency, String serviceType, String networkType, int installYear, 
            String indoorOutdoor, String wifiEnv, double lat, double lng, String workTime) {
		this.id = id;
		this.district = district;
		this.wifi_name = wifiName;
		this.road_address = roadAddress;
		this.detailed_address = detailedAddress;
		this.install_floor = installFloor;
		this.install_type = installType;
		this.install_agency = installAgency;
		this.service_type = serviceType;
		this.network_type = networkType;
		this.install_year = installYear;
		this.indoor_outdoor = indoorOutdoor;
		this.wifi_env = wifiEnv;
		this.lat = lat;
		this.lng = lng;
		this.work_time = workTime;
    }
}
