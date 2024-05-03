package com.bank_of_korea.bank_of_korea.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="users")
@Data
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String email;

    private String password;

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
    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }
}
