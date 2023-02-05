package vn.bk.controllers.dynasty;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import vn.bk.controllers.MenuController;
import vn.bk.models.dynasty.Dynasty;
import vn.bk.repositories.dynasty.DynastyRepository;
import vn.bk.repositories.dynasty.IDynastyRepository;
import vn.bk.views.dynasty.DynastyView;

public class DynastyController {
    private final DynastyView dynastyView;
    private final IDynastyRepository dynastyRepository;

    public DynastyController() {
        this.dynastyView = new DynastyView();
        this.dynastyRepository = new DynastyRepository();
        this.dynastyView.addListener(new DynastyController.DynastyListener());
    }
    
    class DynastyListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == dynastyView.getjButton1()) {
                List<Dynasty> results = dynastyRepository.searchByTitle(dynastyView.getjTextField1().getText());
                if(results == null || results.isEmpty()) return;
                
                DefaultTableModel model = (DefaultTableModel) dynastyView.getjTable1().getModel();
                model.setRowCount(0);
                for (Dynasty dynasty : results) {
                    model.addRow(dynasty.toObject());
                }
            }
            
            if (e.getSource() == dynastyView.getjButton2()) {
                setVisible(false);
                MenuController menuController = new MenuController();
                menuController.setVisible(true);
            }

        }

    }
    
    public void setVisible(boolean flag){
        this.dynastyView.setVisible(flag);
    }
}
