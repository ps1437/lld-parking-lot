package com.syscho.lld.parkinglot;

import lombok.Getter;

import java.util.List;

@Getter
public class Parking {
    private String parkingId;
    private List<ParkingSpot> parkingSpots;

    public Parking(String parkingId, List<ParkingSpot> parkingSpots) {
        this.parkingId = parkingId;
        this.parkingSpots = parkingSpots;
    }

    public boolean isParkingAvailable(VehicleType type) {
        return parkingSpots.stream()
                .anyMatch(s -> s.isFree() && s.getAllowedVehicleType() == type);
    }


    public boolean isParkingFull() {
        return parkingSpots.stream().noneMatch(ParkingSpot::isFree);
    }


    public boolean park(Vehicle vehicle) {
        for (ParkingSpot spot : parkingSpots) {
            if (spot.isFree() && spot.getAllowedVehicleType() == vehicle.getVehicleType()) {
                if (spot.park(vehicle)) {
                    System.out.println(vehicle.getVehicleNumber() + " Parked at spot: " + spot.getParkSpotId());
                    return true;
                }
            }
        }
        System.out.println("Parking is Not available for " + vehicle.getVehicleType());
        return false;
    }

    public Vehicle unPark(Vehicle vehicle) {
        for (ParkingSpot spot : parkingSpots) {
            if (!spot.isFree() && spot.getVehicle().getVehicleNumber().equals(vehicle.getVehicleNumber())) {
                return spot.unPark();
            }
        }
        System.out.println("Vehicle not found with number " + vehicle.getVehicleNumber());
        return null;
    }

    public void printStatus() {
        System.out.println("======= Parking Lot Status =======");
        System.out.printf("%-10s | %-10s | %-10s | %-10s | %-20s%n", "Spot ID", "Type", "Status", "Vehicle", "Parked Time");
        System.out.println("--------------------------------------------------------------");

        for (ParkingSpot spot : parkingSpots) {
            String status = spot.isFree() ? "Free" : "Occupied";
            String vehicle = (spot.getVehicle() != null) ? spot.getVehicle().getVehicleNumber() : "-";
            String parkedTime = (spot.getParkedTime() != null) ? spot.getParkedTime().toString() : "-";
            System.out.printf("%-10s | %-10s | %-10s | %-10s | %-20s%n",
                    spot.getParkSpotId(),
                    spot.getAllowedVehicleType(),
                    status,
                    vehicle,
                    parkedTime);
        }

        long available = parkingSpots.stream().filter(ParkingSpot::isFree).count();
        System.out.println("--------------------------------------------------------------");
        System.out.printf("Total: %d | Free: %d | Occupied: %d%n", parkingSpots.size(), available, parkingSpots.size() - available);
    }

}
