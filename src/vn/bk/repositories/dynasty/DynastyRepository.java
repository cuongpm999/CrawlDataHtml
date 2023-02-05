package vn.bk.repositories.dynasty;

import com.fasterxml.jackson.core.type.TypeReference;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import vn.bk.config.Constant;
import vn.bk.json.MyObjectMapper;
import vn.bk.models.dynasty.Dynasty;

public class DynastyRepository implements IDynastyRepository{

    private List<Dynasty> results = new ArrayList<>();
    
    @Override
    public void addOne(Dynasty dynasty) {
        results.add(dynasty);
    }

    @Override
    public void addMany(List<Dynasty> dynasties) {
        results.addAll(dynasties);
    }

    @Override
    public void save() {
        try {
            File file = new File(Constant.SOURCE_DYNASTY);
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
    public List<Dynasty> loadAllData() {
        try {
            File file = new File(Constant.SOURCE_DYNASTY);
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
            
            return MyObjectMapper.get().readValue(stringBuilder.toString(), new TypeReference<List<Dynasty>>(){});
        } catch (FileNotFoundException ex) {
           ex.printStackTrace();
            return null;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Dynasty> searchByTitle(String title) {
        List<Dynasty> dynasties = loadAllData();
        if(dynasties == null) return null;
        return dynasties.stream().filter(dynasty -> dynasty.getTitle().toLowerCase().contains(title.toLowerCase())).collect(Collectors.toList());
    }
    
}
