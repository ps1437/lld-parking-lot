package com.syscho.lld.parkinglot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class ParkingMain {

    private static final Parking parkingArea = setupParkingFloorArea();

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);

        System.out.println("-----------------------------------------------");
        System.out.println("Multithreaded Parking Started...");

        List<Callable<Void>> parkTasks = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            int finalI = i;
            parkTasks.add(() -> {
                VehicleType type = finalI % 2 == 0 ? VehicleType.CAR : VehicleType.BIKE;
                Vehicle v = new Vehicle("TA-" + (2000 + finalI), type);
                park(v);
                return null;
            });
        }

        //Run all the task concurrently
        executor.invokeAll(parkTasks);

        parkingArea.printStatus();

        Thread.sleep(2000);

        System.out.println("-----------------------------------------------");
        System.out.println("Multithreaded Unparking Started...");

        List<Callable<Void>> unParkTasks = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            unParkTasks.add(() -> {
                VehicleType type = finalI % 2 == 0 ? VehicleType.CAR : VehicleType.BIKE;
                Vehicle v = new Vehicle("TA-" + (2000 + finalI), type);
                unPark(v);
                return null;
            });
        }

        //Run all the task concurrently
        executor.invokeAll(unParkTasks);

        parkingArea.printStatus();
        executor.shutdown();
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
