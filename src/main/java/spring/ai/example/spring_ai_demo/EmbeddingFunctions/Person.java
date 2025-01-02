package spring.ai.example.spring_ai_demo.EmbeddingFunctions;

public class Person {
    private String name;
    
    private String occupation;
    private String notableWorks;
    private String voiceRoles;

    public Person(String name, String occupation, String notableWorks, String voiceRoles) {
        this.name = name;
        this.occupation = occupation;
        this.notableWorks = notableWorks;
        this.voiceRoles = voiceRoles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotableWorks() {
        return notableWorks;
    }

    public void setNotableWorks(String notableWorks) {
        this.notableWorks = notableWorks;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getVoiceRoles() {
        return voiceRoles;
    }

    public void setVoiceRoles(String voiceRoles) {
        this.voiceRoles = voiceRoles;
    }
}
