/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package vn.bk.repositories.event;

import java.util.List;
import vn.bk.models.event.HistoricalEvent;

/**
 *
 * @author cuongpham
 */
public interface IHistoricalEventRepository {
    public void addOne(HistoricalEvent event);
    public void addMany(List<HistoricalEvent> events);
    public void save();
    public List<HistoricalEvent> loadAllData();
    public List<HistoricalEvent> searchByName(String name);
}
