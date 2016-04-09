package com.example.owner.skymood.model.DAO;

import java.util.ArrayList;

/**
 * Created by owner on 05/04/2016.
 */
public interface IMyLocationDAO {

    ArrayList<String> getAllMyLocations();
    long insertMyLocation(String city, String country);
    String selectMyCity(String city);
    long deleteMyLocation(String city);
}
