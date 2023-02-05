/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.bk.models.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.bk.models.BaseModel;

/**
 *
 * @author cuongpham
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoricalEvent extends BaseModel{
    private String name;
    private String time;
    private String location;
    private String result;
    private String note;
    
    public Object[] toObject() {
        return new Object[]{getId(), name, time, location, result, note};
    }
    
}
