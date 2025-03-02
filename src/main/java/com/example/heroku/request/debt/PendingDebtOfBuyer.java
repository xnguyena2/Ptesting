package com.example.heroku.request.debt;

import com.example.heroku.model.DebtTransation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PendingDebtOfBuyer {
    private String group_id;
    private String device_id;
    private DebtTransation.TType type;
}
