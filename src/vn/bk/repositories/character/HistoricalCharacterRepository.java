/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.bk.repositories.character;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.json.JSONArray;
import vn.bk.config.Constant;
import vn.bk.json.MyObjectMapper;
import vn.bk.models.character.HistoricalCharacter;

/**
 *
 * @author cuongpham
 */
public class HistoricalCharacterRepository implements IHistoricalCharacterRepository {

    private List<HistoricalCharacter> results = new ArrayList<>();

    @Override
    public void addOne(HistoricalCharacter character) {
        results.add(character);
    }

    @Override
    public void addMany(List<HistoricalCharacter> characters) {
        results.addAll(characters);
    }

    @Override
    public void save() {
        try {
            File file = new File(Constant.SOURCE_HISTORICAL_CHARACTER);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(MyObjectMapper.get().writeValueAsString(results));
            fileWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public List<HistoricalCharacter> loadAllData() {
        try {
            File file = new File(Constant.SOURCE_HISTORICAL_CHARACTER);
            if (!file.exists()) {
                return null;
            }
            FileReader fileReader = new FileReader(file);

            BufferedReader reader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            
            if(stringBuilder.length() == 0) return null;
            
            return MyObjectMapper.get().readValue(stringBuilder.toString(), new TypeReference<List<HistoricalCharacter>>(){});
        } catch (FileNotFoundException ex) {
           ex.printStackTrace();
            return null;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public List<HistoricalCharacter> searchByName(String name) {
        List<HistoricalCharacter> characters = loadAllData();
        if(characters == null) return null;
        return characters.stream().filter(character -> character.getFullName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList());
    }
    

}
