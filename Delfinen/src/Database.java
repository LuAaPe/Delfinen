import java.util.ArrayList;

public class Database {
    ArrayList<Member> members = new ArrayList<>();

    public Database(){}
    public void addMember(Member member){
        members.add(member);
    }



    @Override
    public String toString(){
        String s ="";
        for (Member m : members){
            s += m + "\n";
        }
        return s;
    }

}
