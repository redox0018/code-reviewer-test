package com.example.tax.service;

import com.example.tax.model.TaxCalculation;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.kernel.colors.ColorConstants;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class PDFGenerationService {
    
    private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("#,##,###.00");

    public void generateITR(TaxCalculation taxCalculation, String outputPath) {
        try {
            File file = new File(outputPath);
            file.getParentFile().mkdirs();
            
            PdfWriter writer = new PdfWriter(outputPath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Add Header
            addHeader(document, taxCalculation);
            
            // Add Income Details
            addIncomeSection(document, taxCalculation);
            
            // Add Deductions
            addDeductionsSection(document, taxCalculation);
            
            // Add Tax Calculation
            addTaxCalculationSection(document, taxCalculation);
            
            // Add Footer
            addFooter(document);

            document.close();
            log.info("PDF generated successfully at: {}", outputPath);
            
        } catch (Exception e) {
            log.error("Error generating PDF: ", e);
            throw new RuntimeException("Failed to generate ITR PDF", e);
        }
    }

    private void addHeader(Document document, TaxCalculation calc) {
        Paragraph header = new Paragraph("INCOME TAX RETURN STATEMENT")
            .setTextAlignment(TextAlignment.CENTER)
            .setBold()
            .setFontSize(16);
        document.add(header);

        Table infoTable = new Table(2);
        infoTable.setWidth(500);
        
        addTableRow(infoTable, "Assessment Year", calc.getAssessmentYear());
        addTableRow(infoTable, "PAN", calc.getPanNumber());
        addTableRow(infoTable, "Name", calc.getTaxpayerName());
        addTableRow(infoTable, "Tax Regime", calc.getRegime());
        
        document.add(infoTable);
        document.add(new Paragraph("\n"));
    }

    private void addIncomeSection(Document document, TaxCalculation calc) {
        document.add(new Paragraph("Income Details").setBold().setFontSize(14));
        
        Table incomeTable = new Table(2);
        incomeTable.setWidth(500);
        
        addTableRow(incomeTable, "Salary Income", formatCurrency(calc.getIncome().getSalary()));
        addTableRow(incomeTable, "HRA Received", formatCurrency(calc.getIncome().getHraReceived()));
        addTableRow(incomeTable, "LTA", formatCurrency(calc.getIncome().getLta()));
        addTableRow(incomeTable, "Other Allowances", formatCurrency(calc.getIncome().getOtherAllowances()));
        addTableRow(incomeTable, "Rental Income", formatCurrency(calc.getIncome().getRentalIncome()));
        addTableRow(incomeTable, "Interest Income", formatCurrency(calc.getIncome().getInterestIncome()));
        addTableRow(incomeTable, "Business Income", formatCurrency(calc.getIncome().getBusinessIncome()));
        addTableRow(incomeTable, "Capital Gains", formatCurrency(calc.getIncome().getCapitalGains()));
        addTableRow(incomeTable, "Total Income", formatCurrency(calc.getIncome().getTotalIncome()));
        
        document.add(incomeTable);
        document.add(new Paragraph("\n"));
    }

    private void addDeductionsSection(Document document, TaxCalculation calc) {
        document.add(new Paragraph("Deductions").setBold().setFontSize(14));
        
        Table deductionsTable = new Table(2);
        deductionsTable.setWidth(500);
        
        addTableRow(deductionsTable, "Section 80C (PPF, ELSS, EPF, etc.)", 
                   formatCurrency(calc.getDeductions().getTotal80CDeductions()));
        addTableRow(deductionsTable, "Section 80D (Health Insurance)", 
                   formatCurrency(calc.getDeductions().getTotalHealthDeductions()));
        addTableRow(deductionsTable, "NPS (80CCD)", formatCurrency(calc.getDeductions().getNps()));
        addTableRow(deductionsTable, "Education Loan Interest (80E)", 
                   formatCurrency(calc.getDeductions().getEducationLoanInterest()));
        addTableRow(deductionsTable, "Donations (80G)", formatCurrency(calc.getDeductions().getDonations()));
        addTableRow(deductionsTable, "Home Loan Interest", formatCurrency(calc.getDeductions().getHomeLoanInterest()));
        addTableRow(deductionsTable, "HRA Exemption", 
                   formatCurrency(calc.getDeductions().calculateHRAExemption(calc.getIncome().getSalary())));
        addTableRow(deductionsTable, "Total Deductions", formatCurrency(calc.getDeductions().getTotalDeductions()));
        
        document.add(deductionsTable);
        document.add(new Paragraph("\n"));
    }

    private void addTaxCalculationSection(Document document, TaxCalculation calc) {
        document.add(new Paragraph("Tax Calculation").setBold().setFontSize(14));
        
        Table taxTable = new Table(2);
        taxTable.setWidth(500);
        
        addTableRow(taxTable, "Taxable Income", formatCurrency(calc.getTaxableIncome()));
        addTableRow(taxTable, "Basic Tax", formatCurrency(calc.getBasicTax()));
        addTableRow(taxTable, "Surcharge", formatCurrency(calc.getSurcharge()));
        addTableRow(taxTable, "Education Cess", formatCurrency(calc.getEducationCess()));
        addTableRow(taxTable, "Total Tax Liability", formatCurrency(calc.getTotalTaxLiability()));
        
        document.add(taxTable);
    }

    private void addFooter(Document document) {
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Generated on: " + 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))
            .setFontSize(10)
            .setTextAlignment(TextAlignment.RIGHT));
    }

    private void addTableRow(Table table, String label, String value) {
        table.addCell(new Cell().add(new Paragraph(label)).setBold());
        table.addCell(new Cell().add(new Paragraph(value)));
    }

    private String formatCurrency(double amount) {
        return "₹ " + CURRENCY_FORMAT.format(amount);
    }
}
