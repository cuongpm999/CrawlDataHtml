/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.bk.controllers.character;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import vn.bk.controllers.MenuController;
import vn.bk.models.character.HistoricalCharacter;
import vn.bk.repositories.character.HistoricalCharacterRepository;
import vn.bk.repositories.character.IHistoricalCharacterRepository;
import vn.bk.views.character.CharacterView;

/**
 *
 * @author cuongpham
 */
public class CharacterController {
    private final CharacterView characterView;
    private final IHistoricalCharacterRepository historicalCharacterRepository;

    public CharacterController() {
        this.characterView = new CharacterView();
        this.historicalCharacterRepository = new HistoricalCharacterRepository();
        this.characterView.addListener(new CharacterListener());
    }
    
    class CharacterListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == characterView.getjButton1()) {
                List<HistoricalCharacter> results = historicalCharacterRepository.searchByName(characterView.getjTextField1().getText());
                if(results == null || results.isEmpty()) return;
                
                DefaultTableModel model = (DefaultTableModel) characterView.getjTable1().getModel();
                model.setRowCount(0);
                for (HistoricalCharacter character : results) {
                    model.addRow(character.toObject());
                }
            }
            
            if (e.getSource() == characterView.getjButton2()) {
                setVisible(false);
                MenuController menuController = new MenuController();
                menuController.setVisible(true);
            }

        }

    }
    
    public void setVisible(boolean flag){
        this.characterView.setVisible(flag);
    }
}
