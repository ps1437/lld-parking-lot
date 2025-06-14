package com.syscho.lld.parkinglot;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ParkingSpot {
    private final String parkSpotId;
    private final VehicleType allowedVehicleType;
    private Vehicle vehicle;
    private LocalDateTime parkedTime = null;
    private boolean isFree;

    public ParkingSpot(String parkSpotId, VehicleType allowedVehicleType) {
        this.parkSpotId = parkSpotId;
        this.isFree = true;
        this.allowedVehicleType = allowedVehicleType;
    }


    public boolean park(Vehicle vehicle) {
        if (!isFree || vehicle.getVehicleType() != allowedVehicleType) return false;
        this.vehicle = vehicle;
        this.isFree = false;
        this.parkedTime = LocalDateTime.now();
        return true;
    }

    public Vehicle unPark() {
        Vehicle parkedVehicle = this.vehicle;
        this.vehicle = null;
        this.isFree = true;
        this.parkedTime = null;
        return parkedVehicle;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public long getParkedDurationMinutes() {
        return parkedTime == null ? 0 : java.time.Duration.between(parkedTime, LocalDateTime.now()).toMinutes();
    }

    @Override
    public String toString() {
        return "Spot[" + parkSpotId + "] - " + (isFree ? "Free" : vehicle.getVehicleNumber()) +
                " (" + allowedVehicleType + ")";
    }
}
