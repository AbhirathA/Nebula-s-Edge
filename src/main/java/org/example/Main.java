package org.example;

public class Main
{
    public static void main(String [] args) throws Exception
    {
        ServerFirebase firebase = ServerFirebase.getInstance();
        firebase.createUser("hello@gmail.com", "1123@AVRa");
        String token = ClientFirebase.signIn("hello@gmail.com", "1123@AVRa");
        System.out.println(token);
    }
}
