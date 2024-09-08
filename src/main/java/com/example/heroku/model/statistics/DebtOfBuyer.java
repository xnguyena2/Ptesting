package com.example.heroku.model.statistics;

import com.example.heroku.response.BuyerData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DebtOfBuyer extends BuyerData {
    private float in_come;
    private float out_come;

    public DebtOfBuyer(DebtOfBuyer s) {
        super(s);
        setIn_come(s.in_come);
        setOut_come(s.out_come);
    }
}
