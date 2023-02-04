/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.bk.models;

import lombok.Data;

/**
 *
 * @author cuongpham
 */
@Data
public class BaseModel {

    private long id;
    private static long sID = 1;

    public BaseModel() {
        this.id = sID;
        sIDIncrement();
    }
    
    

    public void sIDIncrement() {
        this.sID++;
    }
}
