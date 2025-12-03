import UI.Menu;
import controller.MemberController;
import domain.Member;

import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        MemberController memberController = new MemberController();


        memberController.addNewMember("testFirstName", "testSurname", LocalDate.now(),
                false, true, false);
    }





}