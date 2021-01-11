package com.example.heroku.request.voucher;

import com.example.heroku.model.Voucher;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoucherData {
    private Voucher voucher;
    private String[] listUser;
    private String[] listBeer;
}
