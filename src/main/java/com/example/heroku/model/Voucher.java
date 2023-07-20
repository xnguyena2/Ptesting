package com.example.heroku.model;

import com.example.heroku.util.Util;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name="voucher")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Voucher {

    @Id
    String id;

    private String voucher_second_id;

    private String detail;

    private float discount;

    private float amount;

    private int reuse;

    private Timestamp date_expire;

    private Status status;

    private Timestamp createat;

    private boolean for_all_product;

    private boolean for_all_user;

    private boolean package_voucher;

    public Voucher AutoFill() {
        this.createat = new Timestamp(new Date().getTime());
        return this;
    }

    public void comsumeVoucherSync(String transactionKey, SyncRunner runner) {
        String key = transactionKey + this.getVoucher_second_id();
        synchronized (Util.getInstance().GetMap(key)) {
            System.out.println("Begin consume!");
            if (this.reuse == -1) {
                runner.doSync(this.reuse);
            } else {
                int shareResue = Util.getInstance().getVoucher(key);
                System.out.println("ID: " + key + ", get resue from sync: " + shareResue);
                if (shareResue == -1) {
                    shareResue = this.reuse;
                }
                System.out.println("ID: " + key + ", sync resue: " + shareResue + ", db reuse: " + this.reuse);
                if (shareResue > 0) {
                    runner.doSync(shareResue);
                    shareResue--;
                    Util.getInstance().setVoucher(key, shareResue);
                }
            }
            System.out.println("End consume!");
        }
    }

    public enum Status {

        AVARIABLE("avariable");


        private String name;

        Status(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        private static final Map<String, Status> lookup = new HashMap<>();

        static {
            for (Status sts : Status.values()) {
                lookup.put(sts.getName(), sts);
            }
        }

        public static Status get(String text) {
            try {
                Status val = lookup.get(text);
                if (val == null) {
                    return AVARIABLE;
                }
                return val;
            } catch (Exception ex) {
                ex.printStackTrace();
                return AVARIABLE;
            }
        }

        @Override
        public String toString() {
            return this.name;
        }

    }

    public interface SyncRunner {
        void doSync(int reuse);
    }

    public boolean IsOnlyApplyForProduct(String productVOucher) {
        System.out.println("checking voucher: " + voucher_second_id + ", productVOucher: " + productVOucher);
        if (voucher_second_id == null)
            return false;
        return !isFor_all_product() && voucher_second_id.equals(productVOucher);
    }
}
