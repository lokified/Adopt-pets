
package com.loki.yourpet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Generated;
import org.parceler.Parcel;

@Parcel
@Generated("jsonschema2pojo")
public class Breeds {

    @SerializedName("primary")
    @Expose
    private String primary;

    @SerializedName("mixed")
    @Expose
    private Boolean mixed;
    @SerializedName("unknown")
    @Expose
    private Boolean unknown;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Breeds() {
    }

    /**
     * 
     * @param secondary
     * @param mixed
     * @param primary
     * @param unknown
     */
    public Breeds(String primary, Boolean mixed, Boolean unknown) {
        super();
        this.primary = primary;
        this.mixed = mixed;
        this.unknown = unknown;
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public Boolean getMixed() {
        return mixed;
    }

    public void setMixed(Boolean mixed) {
        this.mixed = mixed;
    }

    public Boolean getUnknown() {
        return unknown;
    }

    public void setUnknown(Boolean unknown) {
        this.unknown = unknown;
    }

}
