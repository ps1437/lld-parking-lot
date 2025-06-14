package com.syscho.lld.parkinglot;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Vehicle {
    private String vehicleNumber;
    private VehicleType vehicleType;
}