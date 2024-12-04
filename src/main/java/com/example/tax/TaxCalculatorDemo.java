package com.example.tax;

import com.example.tax.model.Income;
import com.example.tax.model.Deductions;
import com.example.tax.model.TaxCalculation;
import com.example.tax.service.PDFGenerationService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TaxCalculatorDemo {
    public static void main(String[] args) {
        // Create sample income details
        Income income = Income.builder()
                .salary(1200000)           // 12L basic salary
                .hraReceived(240000)       // 2.4L HRA
                .lta(50000)               // 50K LTA
                .otherAllowances(100000)   // 1L other allowances
                .rentalIncome(180000)      // 1.8L rental income
                .interestIncome(50000)     // 50K interest income
                .businessIncome(0)
                .capitalGains(0)
                .build();

        // Create sample deductions
        Deductions deductions = Deductions.builder()
                .ppf(150000)              // 1.5L PPF
                .elss(50000)              // 50K ELSS
                .epf(72000)               // 72K EPF
                .lifeInsurance(25000)     // 25K Life Insurance
                .nps(50000)               // 50K NPS
                .healthInsurance(25000)    // 25K Health Insurance
                .parentHealthInsurance(50000) // 50K Parent's Health Insurance
                .educationLoanInterest(0)
                .donations(10000)          // 10K donations
                .homeLoanInterest(200000)  // 2L Home Loan Interest
                .rentPaid(300000)         // 3L rent paid
                .cityCategory("Metro")
                .build();

        // Create tax calculation
        TaxCalculation taxCalculation = TaxCalculation.builder()
                .income(income)
                .deductions(deductions)
                .assessmentYear("2023-24")
                .panNumber("ABCDE1234F")
                .taxpayerName("John Doe")
                .regime("OLD")
                .build();

        // Calculate tax
        taxCalculation.calculateTax();

        // Log the results
        log.info("Total Income: ₹{}", income.getTotalIncome());
        log.info("Total Deductions: ₹{}", deductions.getTotalDeductions());
        log.info("Taxable Income: ₹{}", taxCalculation.getTaxableIncome());
        log.info("Total Tax Liability: ₹{}", taxCalculation.getTotalTaxLiability());

        // Generate PDF
        PDFGenerationService pdfService = new PDFGenerationService();
        String outputPath = "tax-returns/ITR-" + taxCalculation.getPanNumber() + 
                          "-" + taxCalculation.getAssessmentYear() + ".pdf";
        pdfService.generateITR(taxCalculation, outputPath);
    }
}
