package fr.xebia.extras.selma.beans;

/**
 * Created by dtente on 28/09/2016.
 */
public class PersonDto {

  private String nom;

  private String prenom;

  public PersonDto() {
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  public void setPrenom(String prenom) {
    this.prenom = prenom;
  }

  public String getNom() {
    return this.nom;
  }

  public String getPrenom() {
    return this.prenom;
  }

}
