/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.bk.models.character;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.bk.json.MyObjectMapper;
import vn.bk.models.BaseModel;

/**
 *
 * @author cuongpham
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class HistoricalCharacter extends BaseModel{
    private String fullName;
    private String birth;
    private String death;
    private String father;
    private String mother;
    private String dynasty;
    private String realName;
    private String note;
    
    public Object[] toObject() {
        return new Object[]{getId(), fullName, birth, death, father, mother, dynasty, realName, note};
    }
}
