/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.bk.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import vn.bk.controllers.character.CharacterController;
import vn.bk.controllers.event.EventController;
import vn.bk.models.character.HistoricalCharacter;
import vn.bk.views.MenuView;
import vn.bk.views.character.CharacterView;

/**
 *
 * @author cuongpham
 */
public class MenuController {
    private final MenuView menuView;

    public MenuController() {
        this.menuView = new MenuView();
        this.menuView.addListener(new MenuListener());
    }
    
    public void setVisible(boolean flag){
        this.menuView.setVisible(flag);
    }
    
    class MenuListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == menuView.getjButton1()) {
                setVisible(false);
                CharacterController characterController = new CharacterController();
                characterController.setVisible(true);
            }
            
            if (e.getSource() == menuView.getjButton5()) {
                setVisible(false);
                EventController eventController = new EventController();
                eventController.setVisible(true);
            }

        }

    }
}
