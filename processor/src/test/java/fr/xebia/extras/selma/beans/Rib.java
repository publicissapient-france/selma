package fr.xebia.extras.selma.beans;

public class Rib {

    private Integer codeBanque;

    private Integer codeBanqueCompensation;

    public Rib() {

    }

    public Rib(Integer codeBanque, Integer codeBanqueCompensation) {
        this.codeBanque = codeBanque;
        this.codeBanqueCompensation = codeBanqueCompensation;
    }

    public Integer getCodeBanque() {
        return codeBanque;
    }

    public void setCodeBanque(final Integer codeBanque) {
        this.codeBanque = codeBanque;
    }

    public Integer getCodeBanqueCompensation() {
        return codeBanqueCompensation;
    }

    public void setCodeBanqueCompensation(final Integer codeBanqueCompensation) {
        this.codeBanqueCompensation = codeBanqueCompensation;
    }

}
