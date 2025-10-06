public class CardCredit extends Card {
    private final double limitaCredit;

    public CardCredit(String numarCard, String dataExpirare, String CVV, ContBancar cont, double limitaCredit) {
        super(numarCard, dataExpirare, CVV, cont);
        this.limitaCredit = limitaCredit;
    }

    public double getLimitaCredit() {
        return limitaCredit;
    }

    @Override
    public String toString() {
        return numarCard + " " + dataExpirare + " " + CVV + " " + limitaCredit + " (" + cont.getIBAN() + ")";
    }
}
