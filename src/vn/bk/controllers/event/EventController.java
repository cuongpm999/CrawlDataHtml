/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.bk.controllers.event;

import vn.bk.controllers.character.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import vn.bk.controllers.MenuController;
import vn.bk.models.character.HistoricalCharacter;
import vn.bk.models.event.HistoricalEvent;
import vn.bk.repositories.character.HistoricalCharacterRepository;
import vn.bk.repositories.character.IHistoricalCharacterRepository;
import vn.bk.repositories.event.HistoricalEventRepository;
import vn.bk.repositories.event.IHistoricalEventRepository;
import vn.bk.views.character.CharacterView;
import vn.bk.views.event.EventView;

/**
 *
 * @author cuongpham
 */
public class EventController {
    private final EventView eventView;
    private final IHistoricalEventRepository historicalEventRepository;

    public EventController() {
        this.eventView = new EventView();
        this.historicalEventRepository = new HistoricalEventRepository();
        this.eventView.addListener(new EventListener());
    }
    
    class EventListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == eventView.getjButton1()) {
                List<HistoricalEvent> results = historicalEventRepository.searchByName(eventView.getjTextField1().getText());
                if(results == null || results.isEmpty()) return;
                
                DefaultTableModel model = (DefaultTableModel) eventView.getjTable1().getModel();
                model.setRowCount(0);
                for (HistoricalEvent event : results) {
                    model.addRow(event.toObject());
                }
            }
            
            if (e.getSource() == eventView.getjButton2()) {
                setVisible(false);
                MenuController menuController = new MenuController();
                menuController.setVisible(true);
            }

        }

    }
    
    public void setVisible(boolean flag){
        this.eventView.setVisible(flag);
    }
}
