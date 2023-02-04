/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package vn.bk.repositories.character;

import java.util.List;
import vn.bk.models.character.HistoricalCharacter;

/**
 *
 * @author cuongpham
 */
public interface IHistoricalCharacterRepository {
    public void addOne(HistoricalCharacter character);
    public void addMany(List<HistoricalCharacter> characters);
    public void save();
    public List<HistoricalCharacter> loadAllData();
    public List<HistoricalCharacter> searchByName(String name);
}
