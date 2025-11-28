import java.time.LocalDate;

public class Member {
    String name;
    LocalDate birthDate;

    public Member(String name, LocalDate birthDate){
        this.name = name;
        this.birthDate = birthDate;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthDate(){
        return birthDate;
    }

    public String toString(){
        return getName()+" "+getBirthDate();
    }
}
