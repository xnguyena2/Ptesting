package com.example.heroku.model;

import com.example.heroku.util.Util;
import com.example.heroku.model.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="payment_transaction")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTransation extends BaseEntity {

    private String transaction_second_id;

    private String device_id;

    private String package_second_id;

    private float amount;

    private String note;

    private String category;

    private String money_source;

    private TType transaction_type;

    private Status status;


    public PaymentTransation AutoFill() {
        if (transaction_second_id == null) {
            transaction_second_id = Util.getInstance().GenerateID();
        }
        if (status == null) {
            status = Status.CREATE;
        }
        return (PaymentTransation) super.AutoFillIfNull();
    }


    public enum TType {
        INCOME("INCOME"),
        OUTCOME("OUTCOME");



        private String name;

        TType(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }

        private static final Map<String, TType> lookup = new HashMap<>();

        static
        {
            for(TType sts : TType.values())
            {
                lookup.put(sts.getName(), sts);
            }
        }

        public static TType get(String text)
        {
            try {
                TType val = lookup.get(text);
                if(val == null){
                    return OUTCOME;
                }
                return val;
            } catch (Exception ex) {
                ex.printStackTrace();
                return OUTCOME;
            }
        }

        @Override
        public String toString() {
            return this.name;
        }
    }


    public enum Status {
        CREATE("CREATE"),
        DONE("DONE");



        private String name;

        Status(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }

        private static final Map<String, Status> lookup = new HashMap<>();

        static
        {
            for(Status sts : Status.values())
            {
                lookup.put(sts.getName(), sts);
            }
        }

        public static Status get(String text)
        {
            try {
                Status val = lookup.get(text);
                if(val == null){
                    return CREATE;
                }
                return val;
            } catch (Exception ex) {
                ex.printStackTrace();
                return CREATE;
            }
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

}