
package com.loki.yourpet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Generated;
import org.parceler.Parcel;

@Parcel
@Generated("jsonschema2pojo")
public class Colors {

    @SerializedName("primary")
    @Expose
    private String primary;



    /**
     * No args constructor for use in serialization
     * 
     */
    public Colors() {
    }

    /**
     * @param primary
     */
    public Colors(String primary) {
        super();
        this.primary = primary;
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }


}
