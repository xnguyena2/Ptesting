package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;
import java.util.Date;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity {
    protected String group_id;
    protected Timestamp createat;

    public BaseEntity AutoFill(){
        this.createat = new Timestamp(new Date().getTime());
        return this;
    }
}
