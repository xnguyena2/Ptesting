package com.example.heroku.request.voucher;

import com.example.heroku.model.Voucher;
import com.example.heroku.request.datetime.NgbDateStruct;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
@Builder
public class VoucherData {

    private String group_id;

    private String voucher_second_id;

    private String detail;

    private float discount;

    private float amount;

    private int reuse;

    private String status;

    private boolean for_all_product;

    private boolean for_all_user;

    private boolean package_voucher;

    private NgbDateStruct dateExpir;

    private String[] listUser;
    private String[] listBeer;


    public LocalDateTime GetExpirDateTime() {
        if (dateExpir == null)
            return null;
        if (dateExpir.getDay() == 0 && dateExpir.getMonth() == 0 && dateExpir.getYear() == 0)
            return null;
        return dateExpir.toLocalDateTime();
    }

    public Voucher GetVoucher() {

        return Voucher.builder()
                .group_id(getGroup_id())
                .voucher_second_id(getVoucher_second_id())
                .detail(getDetail())
                .discount(getDiscount())
                .amount(getAmount())
                .reuse(getReuse())
                .date_expire(GetExpirDateTime())
                .status(Voucher.Status.get(getStatus()))
                .for_all_product(isFor_all_product())
                .for_all_user(isFor_all_user())
                .package_voucher(isPackage_voucher())
                .build();
    }

    public VoucherData FromVoucher(Voucher v) {
        System.out.println("voucher second id: " + v.getVoucher_second_id());
        setGroup_id(v.getGroup_id());
        setVoucher_second_id(v.getVoucher_second_id());
        setDetail(v.getDetail());
        setDiscount(v.getDiscount());
        setAmount(v.getAmount());
        setReuse(v.getReuse());
        setDateExpir(NgbDateStruct.fromLocalDateTime(v.getDate_expire()));
        setStatus(v.getStatus().getName());
        setFor_all_product(v.isFor_all_product());
        setFor_all_user(v.isFor_all_user());
        return this;
    }

    public VoucherData SetListUser(ArrayList<String> listuser) {
        int length = listuser.size();
        System.out.println("user size: " + length);
        this.listUser = new String[length];
        for (int i = 0; i < length; i++) {
            this.listUser[i] = listuser.get(i);
        }
        return this;
    }

    public VoucherData SetListProduct(ArrayList<String> listProduct) {
        int length = listProduct.size();
        System.out.println("product size: " + length);
        this.listBeer = new String[length];
        for (int i = 0; i < length; i++) {
            this.listBeer[i] = listProduct.get(i);
        }
        return this;
    }
}
