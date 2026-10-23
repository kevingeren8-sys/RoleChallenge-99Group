package com.exercise.userservice.dto;

public class UserDto{
    private Integer id;
    private String name;
    private long createdAt;
    private long updatedAt;

    public Integer getId(){
        return id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public long getCreatedAt(){
        return createdAt;
    }

    public void setCreatedAt(long createdAt){
        this.createdAt = createdAt;
    }

    public long getUpdatedAt(){
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt){
        this.updatedAt = updatedAt;
    }
}