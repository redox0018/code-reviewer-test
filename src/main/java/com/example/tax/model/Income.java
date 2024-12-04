package com.example.tax.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Income {
    private double salary;              // Basic salary
    private double hraReceived;         // House Rent Allowance
    private double lta;                 // Leave Travel Allowance
    private double otherAllowances;     // Other allowances
    private double rentalIncome;        // Income from house property
    private double interestIncome;      // Interest from savings/deposits
    private double businessIncome;      // Income from business/profession
    private double capitalGains;        // Short-term and long-term capital gains

    public double getTotalIncome() {
        return salary + hraReceived + lta + otherAllowances + 
               rentalIncome + interestIncome + businessIncome + capitalGains;
    }
}
