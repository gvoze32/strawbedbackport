public class CheckBedItem {
    public static void main(String[] args) {
        try {
            Class<?> clazz = Class.forName("net.minecraft.world.item.BedItem");
            System.out.println("BedItem exists!");
        } catch (Exception e) {
            System.out.println("No BedItem: " + e.getMessage());
        }
    }
}
