package vn.bk.repositories.dynasty;

import java.util.List;
import vn.bk.models.dynasty.Dynasty;

public interface IDynastyRepository {
    public void addOne(Dynasty dynasty);
    public void addMany(List<Dynasty> dynasties);
    public void save();
    public List<Dynasty> loadAllData();
    public List<Dynasty> searchByTitle(String title);
}
