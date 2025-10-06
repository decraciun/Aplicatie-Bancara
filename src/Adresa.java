public class Adresa {
    private String judet;
    private String oras;
    private String strada;
    private String numar;
    private String bloc;
    private String scara;
    private String apartament;

    public Adresa(String judet, String oras, String strada, String numar) {
        this.judet = judet;
        this.oras = oras;
        this.strada = strada;
        this.numar = numar;
        this.bloc = null;
        this.scara = null;
        this.apartament = null;
    }

    public Adresa(String judet, String oras, String strada, String numar, String bloc, String scara, String apartament) {
        this.judet = judet;
        this.oras = oras;
        this.strada = strada;
        this.numar = numar;
        this.bloc = bloc;
        this.scara = scara;
        this.apartament = apartament;
    }

    public String getJudet() { return judet; }

    public String getOras() { return oras; }

    public String getStrada() { return strada; }

    public String getNumar() { return numar; }

    public String getBloc() { return bloc; }

    public String getScara() { return scara; }

    public String getApartament() { return apartament; }

    public void setJudet(String judet) { this.judet = judet; }

    public void setOras(String oras) { this.oras = oras; }

    public void setStrada(String strada) { this.strada = strada; }

    public void setNumar(String numar) { this.numar = numar; }

    public void setBloc(String bloc) { this.bloc = bloc; }

    public void setScara(String scara) { this.scara = scara; }

    public void setApartament(String apartament) { this.apartament = apartament; }

    @Override
    public String toString() {
        if (bloc == null && scara == null && apartament == null)
            return judet + ", " + oras + ", " + strada + ", " + numar;
        else
            return judet + ", " + oras + ", " + strada + ", " + numar + ", " + bloc + ", " + scara + ", " + apartament;
    }
}
