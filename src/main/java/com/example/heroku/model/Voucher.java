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

    private boolean for_all_beer;

    private boolean for_all_user;

    public Voucher AutoFill() {
        this.createat = new Timestamp(new Date().getTime());
        return this;
    }

    public void comsumeVoucherSync(String transactionKey, SyncRunner runner) {
        String key = transactionKey + this.getVoucher_second_id();
        synchronized (Util.getInstance().GetMap(key)) {
            System.out.println("Begin consume!");
            int shareResue = Util.getInstance().getVoucher(key);
            if (shareResue == -1) {
                shareResue = this.reuse;
            }
            System.out.println("ID: " + key + ", resue: " + shareResue);
            if (shareResue > 0) {
                runner.doSync(shareResue);
                shareResue--;
                Util.getInstance().setVoucher(key, shareResue);
            }
            System.out.println("End consume!");
        }
    }

    public enum Status {

    }

    public interface SyncRunner {
        void doSync(int reuse);
    }
}
