/**
 * Pricing tier lookup for the membership store.
 *
 * Part of the checkout flow: members belong to a discount tier (Standard,
 * Silver, Gold, Platinum, or Founder), and this class looks up the
 * discount rate for a given tier when calculating the final price.
 *
 * NOTE FOR STUDENTS: this file is part of the application you are analysing.
 * You are not expected to have written it — your job is to find, verify, and
 * fix the vulnerability(ies) it contains.
 */
public class PricingService {

    // Discount applied per membership tier.
    // Index 0 = Standard, 1 = Silver, 2 = Gold, 3 = Platinum, 4 = Founder
    private static final double[] TIER_DISCOUNTS = { 0.00, 0.05, 0.10, 0.15, 0.20 };

    /**
     * Returns the discount rate for a given membership tier index.
     *
     * The tier index reflects the member's account tier (0-4, per the
     * table above). In the real application this value is read from the
     * member's account record, which in turn traces back to a signup
     * form, an admin update, or — as exercised here — a request
     * parameter supplied directly by the caller.
     */
    public static double getDiscountForTier(int tierIndex) {
        return TIER_DISCOUNTS[tierIndex];
    }

    /**
     * Applies the tier discount to a price at checkout.
     */
    public static double applyDiscount(double price, int tierIndex) {
        double discount = getDiscountForTier(tierIndex);
        return price - (price * discount);
    }

    // Simple manual entry point so this class can be run/tested standalone
    // without needing the rest of the app.
    public static void main(String[] args) {
        int tierIndex = Integer.parseInt(args[0]);
        double price = 100.0;
        System.out.println("Price after discount: " + applyDiscount(price, tierIndex));
    }
}
