package Model;


import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by yahia on 19/07/17.
 */

public class City_Area_Model {
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    String id = "";
    String name = "" ;
}
