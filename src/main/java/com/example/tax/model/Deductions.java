package com.example.tax.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Deductions {
    // Section 80C - Investments & payments (max 1.5L)
    private double ppf;                 // Public Provident Fund
    private double elss;                // Equity Linked Savings Scheme
    private double epf;                 // Employee Provident Fund
    private double lifeInsurance;       // Life Insurance Premium
    private double nps;                 // National Pension Scheme (additional 50k under 80CCD(1B))
    
    // Section 80D - Health Insurance
    private double healthInsurance;     // Medical Insurance Premium
    private double parentHealthInsurance; // Parents' Medical Insurance Premium
    
    // Section 80E - Education Loan Interest
    private double educationLoanInterest;
    
    // Section 80G - Charitable Donations
    private double donations;
    
    // Section 24 - Home Loan Interest
    private double homeLoanInterest;
    
    // HRA Exemption components
    private double rentPaid;
    private String cityCategory;        // Metro/Non-Metro
    
    public double getTotal80CDeductions() {
        double total = ppf + elss + epf + lifeInsurance;
        return Math.min(total, 150000); // Max 1.5L under 80C
    }
    
    public double getTotalHealthDeductions() {
        double selfLimit = 25000;
        double parentLimit = 50000; // Assuming parents are senior citizens
        return Math.min(healthInsurance, selfLimit) + 
               Math.min(parentHealthInsurance, parentLimit);
    }
    
    public double getTotalDeductions() {
        return getTotal80CDeductions() +
               getTotalHealthDeductions() +
               Math.min(nps, 50000) +    // Additional NPS deduction
               educationLoanInterest +
               donations +
               homeLoanInterest;
    }
    
    public double calculateHRAExemption(double basicSalary) {
        double rentExceedingTenPercentOfSalary = rentPaid - (0.1 * basicSalary);
        double hraExemptionLimit;
        
        if ("Metro".equalsIgnoreCase(cityCategory)) {
            hraExemptionLimit = 0.5 * basicSalary;  // 50% for metro cities
        } else {
            hraExemptionLimit = 0.4 * basicSalary;  // 40% for non-metro cities
        }
        
        return Math.min(rentExceedingTenPercentOfSalary, hraExemptionLimit);
    }
}
