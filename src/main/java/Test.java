import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Test {
    public void calculateFunction(Long principal) {
        // Calculate monthly salary
        double monthlySalary = principal / 12;

        // Calculate tax slab and net tax paid
        double taxRate = getTaxRate(monthlySalary);
        double annualTax = principal * taxRate;
        double netSalary = principal - annualTax;
        String string = null;
        log.info(String.valueOf(string.length()));
        // Output details using slf4j
        log.info("Monthly Salary: {}", monthlySalary);
        log.info("Tax Rate: {}%", (taxRate * 100));
        log.info("Annual Tax Paid: {}", annualTax);
        log.info("Net Annual Salary: {}", netSalary);
    }

    private static double getTaxRate(double monthlySalary) {
        if (monthlySalary <= 2500) {
            return 0.05; // 5% tax rate
        } else if (monthlySalary <= 5000) {
            return 0.10; // 10% tax rate
        } else if (monthlySalary <= 10000) {
            return 0.20; // 20% tax rate
        } else {
            return 0.30; // 30% tax rate
        }
    }
}
