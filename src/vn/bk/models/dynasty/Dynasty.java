package vn.bk.models.dynasty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.bk.models.BaseModel;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Dynasty extends BaseModel{
    private String title;
    private String description;
    
    public Object[] toObject() {
        return new Object[]{getId(), title, description};
    }
}
