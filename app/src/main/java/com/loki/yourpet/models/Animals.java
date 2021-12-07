
package com.loki.yourpet.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Generated;
import org.parceler.Parcel;

@Generated("jsonschema2pojo")
@Parcel
public class Animals {

    @SerializedName("animals")
    @Expose
    private List<Animal> animals = null;
    @SerializedName("pagination")
    @Expose
    private Pagination pagination;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Animals() {
    }

    /**
     * 
     * @param pagination
     * @param animals
     */
    public Animals(List<Animal> animals, Pagination pagination) {
        super();
        this.animals = animals;
        this.pagination = pagination;
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public void setAnimals(List<Animal> animals) {
        this.animals = animals;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

}
