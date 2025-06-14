package com.syscho.lld.parkinglot;

import lombok.Getter;

import java.util.*;

@Getter
public class Parking {

    public static final int BASE_PRICE = 20;
    private static final int PER_MINUTES_CAR = 4;
    private static final int PER_MINUTES_BIKE = 2;

    private final String parkingId;
    private final List<ParkingSpot> parkingSpots;
    private final Map<VehicleType, Queue<ParkingSpot>> freeSpotsByType = new HashMap<>();
    private final Map<String, ParkingSpot> vehicleNumberToSpot = new HashMap<>();


    public Parking(String parkingId, List<ParkingSpot> parkingSpots) {
        this.parkingId = parkingId;
        this.parkingSpots = parkingSpots;
        for (ParkingSpot spot : parkingSpots) {
            // Grouped by Type during creation
            freeSpotsByType
                    .computeIfAbsent(spot.getAllowedVehicleType(), k -> new LinkedList<>())
                    .add(spot);
        }

    }

    //O(1)
    public boolean park(Vehicle vehicle) {
        Queue<ParkingSpot> availableSpots = freeSpotsByType.get(vehicle.getVehicleType());

        if (availableSpots == null || availableSpots.isEmpty()) {
            System.out.println("Parking is Not available for " + vehicle.getVehicleType());
            return false;
        }

        ParkingSpot spot = availableSpots.poll();
        if (spot.park(vehicle)) {
            vehicleNumberToSpot.put(vehicle.getVehicleNumber(), spot);
            System.out.println(vehicle.getVehicleNumber() + " Parked at spot: " + spot.getParkSpotId());
            return true;
        }
        return false;
    }

    //O(1)
    public Vehicle unPark(Vehicle vehicle) {

        ParkingSpot spot = vehicleNumberToSpot.remove(vehicle.getVehicleNumber());

        if (spot == null) {
            System.out.println("Vehicle not found with number " + vehicle.getVehicleNumber());
            return null;
        }
        Vehicle unParkedVehicle = spot.unPark();
        long duration = spot.getParkedDurationMinutes();
        int totalFare = calculateFee(unParkedVehicle.getVehicleType(), duration);

        // Add Spot back to free queue
        freeSpotsByType.get(spot.getAllowedVehicleType()).offer(spot);

        System.out.println("Unparked " + unParkedVehicle.getVehicleNumber() + " | Duration: " + duration +
                " min | Fee: ₹" + totalFare);

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

    private int calculateFee(VehicleType vehicleType, long minutes) {
        int perMinutesCharges = vehicleType == VehicleType.CAR ? PER_MINUTES_CAR : PER_MINUTES_BIKE;
        return (int) Math.max(BASE_PRICE, minutes * perMinutesCharges);
    }


}
