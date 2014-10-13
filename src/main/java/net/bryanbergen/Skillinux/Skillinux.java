package net.bryanbergen.Skillinux;

import java.util.Scanner;
import net.bryanbergen.Skillinux.APICall.AccountCall;
import net.bryanbergen.Skillinux.APICall.ApiCall;
import net.bryanbergen.Skillinux.Entities.DemoAPI;

public class Skillinux {

    public static void main(String[] args) {

//        System.out.println(DatabaseConnection.getInstance().testCall());
        Scanner query = new Scanner(ApiCall.getInstance().getAccountDocument(new DemoAPI(), AccountCall.Characters));
        while (query.hasNext()) {
            System.out.println(query.next());
        }
    }
}
