package fr.xebia.extras.selma.beans;

public class IbanRest {

    private Integer codeBanque;

    private Integer codeBanqueDomiciliation;

    public IbanRest() {

    }
    
    public IbanRest(Integer codeBanque, Integer codeBanqueDomiciliation) {
	this.codeBanque = codeBanque;
	this.codeBanqueDomiciliation = codeBanqueDomiciliation;
    }

    public Integer getCodeBanque() {
	return codeBanque;
    }

    public void setCodeBanque(final Integer codeBanque) {
	this.codeBanque = codeBanque;
    }

    public Integer getCodeBanqueDomiciliation() {
	return codeBanqueDomiciliation;
    }

    public void setCodeBanqueDomiciliation(final Integer codeBanqueDomiciliation) {
	this.codeBanqueDomiciliation = codeBanqueDomiciliation;
    }
}
