package com.example.tax.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaxCalculation {
    private Income income;
    private Deductions deductions;
    private double taxableIncome;
    private double basicTax;
    private double surcharge;
    private double educationCess;
    private double totalTaxLiability;
    private String assessmentYear;
    private String panNumber;
    private String taxpayerName;
    private String regime;  // OLD or NEW

    public void calculateTaxableIncome() {
        double totalIncome = income.getTotalIncome();
        double totalDeductions = deductions.getTotalDeductions();
        double hraExemption = deductions.calculateHRAExemption(income.getSalary());
        
        // Calculate taxable income after all deductions and exemptions
        this.taxableIncome = totalIncome - totalDeductions - hraExemption;
    }

    public void calculateTax() {
        calculateTaxableIncome();
        
        if ("NEW".equalsIgnoreCase(regime)) {
            calculateNewRegimeTax();
        } else {
            calculateOldRegimeTax();
        }
        
        // Calculate education cess (4%)
        this.educationCess = (basicTax + surcharge) * 0.04;
        
        // Calculate total tax liability
        this.totalTaxLiability = basicTax + surcharge + educationCess;
    }

    private void calculateOldRegimeTax() {
        double remainingIncome = taxableIncome;
        basicTax = 0;

        // Up to 2.5L - No tax
        remainingIncome = Math.max(0, remainingIncome - 250000);

        // 2.5L to 5L - 5%
        if (remainingIncome > 0) {
            double taxableAt5 = Math.min(remainingIncome, 250000);
            basicTax += taxableAt5 * 0.05;
            remainingIncome = Math.max(0, remainingIncome - 250000);
        }

        // 5L to 10L - 20%
        if (remainingIncome > 0) {
            double taxableAt20 = Math.min(remainingIncome, 500000);
            basicTax += taxableAt20 * 0.20;
            remainingIncome = Math.max(0, remainingIncome - 500000);
        }

        // Above 10L - 30%
        if (remainingIncome > 0) {
            basicTax += remainingIncome * 0.30;
        }

        // Calculate surcharge for high income
        if (taxableIncome > 5000000 && taxableIncome <= 10000000) {
            surcharge = basicTax * 0.10;
        } else if (taxableIncome > 10000000) {
            surcharge = basicTax * 0.15;
        }
    }

    private void calculateNewRegimeTax() {
        double remainingIncome = taxableIncome;
        basicTax = 0;

        // Up to 3L - No tax
        remainingIncome = Math.max(0, remainingIncome - 300000);

        // 3L to 6L - 5%
        if (remainingIncome > 0) {
            double taxableAt5 = Math.min(remainingIncome, 300000);
            basicTax += taxableAt5 * 0.05;
            remainingIncome = Math.max(0, remainingIncome - 300000);
        }

        // 6L to 9L - 10%
        if (remainingIncome > 0) {
            double taxableAt10 = Math.min(remainingIncome, 300000);
            basicTax += taxableAt10 * 0.10;
            remainingIncome = Math.max(0, remainingIncome - 300000);
        }

        // 9L to 12L - 15%
        if (remainingIncome > 0) {
            double taxableAt15 = Math.min(remainingIncome, 300000);
            basicTax += taxableAt15 * 0.15;
            remainingIncome = Math.max(0, remainingIncome - 300000);
        }

        // 12L to 15L - 20%
        if (remainingIncome > 0) {
            double taxableAt20 = Math.min(remainingIncome, 300000);
            basicTax += taxableAt20 * 0.20;
            remainingIncome = Math.max(0, remainingIncome - 300000);
        }

        // Above 15L - 30%
        if (remainingIncome > 0) {
            basicTax += remainingIncome * 0.30;
        }

        // Calculate surcharge for high income (same as old regime)
        if (taxableIncome > 5000000 && taxableIncome <= 10000000) {
            surcharge = basicTax * 0.10;
        } else if (taxableIncome > 10000000) {
            surcharge = basicTax * 0.15;
        }
    }
}
