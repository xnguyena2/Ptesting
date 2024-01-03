package com.example.heroku.request.beer;

import com.example.heroku.model.PaymentTransation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPackgeWithTransaction {
    ProductPackage productPackage;
    PaymentTransation transation;
}
