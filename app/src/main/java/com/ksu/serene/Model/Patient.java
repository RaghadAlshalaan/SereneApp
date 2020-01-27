package com.ksu.serene.Model;

public class Patient {
    private String fullName;
    private String email;
    private String GAD7Scalescore;
    private String age;
    private String gender;
    private String height;
    private String weight;
    private String employmentStatus;
    private String maritalStatus;
    private String monthlyIncome;
    private String smokeCigarettes;
    private String chronicDiseases;



    public Patient ( String fullName, String email, String GAD7Scalescore, String age, String gender, String height, String weight, String employmentStatus,
                     String maritalStatus, String monthlyIncome, String smokeCigarettes, String chronicDiseases ){
        this.fullName = fullName;
        this.email = email;
        this.GAD7Scalescore = GAD7Scalescore;
        this.age = age;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.employmentStatus = employmentStatus;
        this.maritalStatus = maritalStatus;
        this.monthlyIncome = monthlyIncome;
        this.smokeCigarettes = smokeCigarettes;
        this.chronicDiseases = chronicDiseases;
    }
// getters
    public String getFullName(){ return fullName;}
    public String getEmail(){return email;}
    public String getGAD7Scalescore(){return GAD7Scalescore;}
    public String getAge(){return age;}
    public String getGender(){return gender;}
    public String getHeight(){return height;}
    public String getWeight(){return weight;}
    public String getEmploymentStatus(){return employmentStatus;}
    public String getMaritalStatus(){return maritalStatus;}
    public String getMonthlyIncome(){return monthlyIncome;}
    public String getSmokeCigarettes(){return smokeCigarettes;}
    public String getChronicDiseases(){return chronicDiseases;}

}
