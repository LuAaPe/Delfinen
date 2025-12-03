import controller.MemberController;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        MemberController memberController = new MemberController();


        memberController.addNewMember("testFirstName", "testSurname", LocalDate.now(),
                false, true, false);
    }





}