package model;

/**
 * Model for Outsourced parts
 */
public class Outsourced extends Part {

    /**
     * Company name for Outsourced part
     */
    private String companyName;

    /**
     * Detailed constructor for creating Outsourced object
     *
     * @param id for ID of the part
     * @param name for name of the part
     * @param stock for stock/inventory of the part
     * @param max for maximum amount of inventory of the part
     * @param min for minimum amount of inventory of the part
     * @param price for price of the part
     * @param companyName for the company name of the Outsourced part
     */
    public Outsourced(int id, String name, int stock, int max, int min, double price, String companyName) {
        super(id, name, price, stock, min, max);
        setCompanyName(companyName);
    }

    /**
     * Gets the company name
     * @return returns the company name
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Sets the company name
     * @param companyName the string company name to assign to Outsourced object
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}




