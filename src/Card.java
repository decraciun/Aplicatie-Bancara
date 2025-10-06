public class Card {
    protected String numarCard;
    protected String dataExpirare;
    protected String CVV;
    protected ContBancar cont;

    public Card(String numarCard, String dataExpirare, String CVV, ContBancar cont) {
        this.numarCard = numarCard;
        this.dataExpirare = dataExpirare;
        this.CVV = CVV;
        this.cont = cont;
    }

    public ContBancar getCont() { return cont; }

    public String getNumarCard() { return numarCard; }
}
