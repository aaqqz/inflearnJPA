package hellojpa;

public class ValueMain {

    public static void main(String[] args) {

        int a = 10;
        int b = 10;

        System.out.println("a == b : " + (a == b));

        Address address1 = new Address("C", "S", "Z");
        Address address2 = new Address("C", "S", "Z");

        System.out.println("address1 == address2 : " + (address1.equals(address2)));

        String A = "a";
        String B = "a";

        System.out.println("A == B : " + (A.equals(B)));
    }
}
