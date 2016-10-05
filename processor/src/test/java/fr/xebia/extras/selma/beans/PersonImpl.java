package fr.xebia.extras.selma.beans;

/**
 * Created by dtente on 28/09/2016.
 */
public class PersonImpl implements Person {

  private String nom;

  private String prenom;

  public PersonImpl() {
  }

  public PersonImpl(String nom, String prenom) {
    this.nom = nom;
    this.prenom = prenom;
  }

  public String getNom() {
    return this.nom;
  }

  public String getPrenom() {
    return this.prenom;
  }

}
