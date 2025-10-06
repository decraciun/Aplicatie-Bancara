public class CardDebit extends Card{

    public CardDebit(String numarCard, String dataExpirare, String CVV, ContBancar cont) {
        super(numarCard, dataExpirare, CVV, cont);
    }

    @Override
    public String toString() {
        return numarCard + " " + dataExpirare + " " + CVV + " (" + cont.getIBAN() + ")";
    }
}
