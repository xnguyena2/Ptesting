package com.example.heroku.request.store;

import com.example.heroku.model.Store;
import com.example.heroku.request.data.UpdatePassword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreInitData {
    private UpdatePassword newAccount;
    private Store store;
}
