package com.bank_of_korea.bank_of_korea.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="bookmark")
@Data
public class BookmarkData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String email;

    private String locations;

    public Integer getId(){
        return id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getLocations(){
        return locations;
    }

    public void setLocations(String locations){
        this.locations = locations;
    }

}
