package com.petmily.domain.enum_type;

public enum LocationType {

    SEOUL("서울특별시"), GYEONGGI("경기도"), GANGWON("강원도"),
    CHUNGCHEONG("충청도"), JEOLLA("전라도"), JEJU("제주도");

    String locationName;

    LocationType(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationName() {
        return locationName;
    }
}
