package com.syscho.lld.parkinglot;

import java.util.stream.IntStream;

public class ParkingMain {
    private static final Parking parkingArea = setupParkingFloorArea();

    public static void main(String[] args) throws InterruptedException {

        System.out.println("-----------------------------------------------");
        System.out.println("Trying to park vehicles...");
        for (int i = 0; i < 22; i++) {
            VehicleType type = i % 2 == 0 ? VehicleType.CAR : VehicleType.BIKE;
            Vehicle v = new Vehicle("TA-" + (2000 + i), type);
            park(v);
        }

        parkingArea.printStatus();
        Thread.sleep(2000);
        System.out.println("-----------------------------------------------");
        System.out.println("Unparking few vehicles...");
        for (int i = 0; i < 5; i++) {
            Vehicle v = new Vehicle("TA-" + (2000 + i), i % 2 == 0 ? VehicleType.CAR : VehicleType.BIKE);
            unPark(v);
        }

        parkingArea.printStatus();
    }

    private static void park(Vehicle vehicle) {
        if (!parkingArea.park(vehicle)) {
            System.out.println("Failed to park " + vehicle.getVehicleNumber());
        }
    }

    private static void unPark(Vehicle vehicle) {
        parkingArea.unPark(vehicle);
    }

    private static Parking setupParkingFloorArea() {
        var spots = IntStream.rangeClosed(1, 20)
                .mapToObj(i -> {
                    final String parkSpotId = "spot" + i;
                    final VehicleType allowedVehicleType = (i % 2 == 0) ? VehicleType.CAR : VehicleType.BIKE;
                    return new ParkingSpot(parkSpotId, allowedVehicleType);
                })
                .toList();
        return new Parking("floor-1", spots);
    }

}
