/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.bk;

import vn.bk.config.Constant;
import vn.bk.controllers.MenuController;
import vn.bk.controllers.character.CharacterController;
import vn.bk.repositories.character.HistoricalCharacterRepository;
import vn.bk.services.character.CrawlHistoricalCharacter;

/**
 *
 * @author cuongpham
 */
public class Main {
    public static void main(String[] args) {
        CrawlHistoricalCharacter crawlHistoricalCharacter = new CrawlHistoricalCharacter();
        
        MenuController menuController = new MenuController();
        menuController.setVisible(true);
    }
    
}
