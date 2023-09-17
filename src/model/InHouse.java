package model;

/**
 * Model for InHouse parts
 */
public class InHouse extends Part {

    /**
     * Machine ID for InHouse part
     */
    private int machineId;

    /**
     * Detailed constructor for creating InHouse object
     *
     * @param id for ID of the part
     * @param name for name of the part
     * @param stock for stock/inventory of the part
     * @param max   for maximum amount of inventory of the part
     * @param min   for minimum amount of inventory of the part
     * @param price for price of the part
     * @param machineId for the machine ID of the InHouse part
     */
    public InHouse(int id, String name, int stock, int max, int min, double price, int machineId) {
        super(id, name, price, stock, min, max);
        setMachineId(machineId);
    }

    /**
     * Gets the machine ID
     *
     * @return returns the machine ID
     */
    public int getMachineId() {
        return machineId;
    }

    /**
     * Sets the machine ID
     * @param machineId the integer machine ID to assign to InHouse object
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}

