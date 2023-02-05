/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.bk.repositories.event;

import vn.bk.repositories.character.*;
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
import vn.bk.models.event.HistoricalEvent;

/**
 *
 * @author cuongpham
 */
public class HistoricalEventRepository implements IHistoricalEventRepository {

    private final List<HistoricalEvent> results = new ArrayList<>();

    @Override
    public void addOne(HistoricalEvent event) {
        results.add(event);
    }

    @Override
    public void addMany(List<HistoricalEvent> events) {
        results.addAll(events);
    }

    @Override
    public void save() {
        try {
            File file = new File(Constant.SOURCE_HISTORICAL_EVENT);
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
    public List<HistoricalEvent> loadAllData() {
        try {
            File file = new File(Constant.SOURCE_HISTORICAL_EVENT);
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
            
            return MyObjectMapper.get().readValue(stringBuilder.toString(), new TypeReference<List<HistoricalEvent>>(){});
        } catch (FileNotFoundException ex) {
           ex.printStackTrace();
            return null;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public List<HistoricalEvent> searchByName(String name) {
        List<HistoricalEvent> events = loadAllData();
        if(events == null) return null;
        return events.stream().filter(event -> event.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList());
    }
    

}
