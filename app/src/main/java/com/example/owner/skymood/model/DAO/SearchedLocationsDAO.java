package com.example.owner.skymood.model.DAO;

/**
 * Created by owner on 05/04/2016.
 */
public class SearchedLocationsDAO implements ISearchedLocations{
    private static SearchedLocationsDAO ourInstance;

    private SearchedLocationsDAO() {
    }

    public static SearchedLocationsDAO getInstance() {
        if(ourInstance == null)
            ourInstance = new SearchedLocationsDAO();
        return ourInstance;
    }


}
