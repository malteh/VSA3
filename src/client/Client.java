package client;

import objects.IPerson;
import objects.PersonProxy;

public class Client {
	public static void main(String[] args) {
		IPerson person = new PersonProxy("Peter");
		System.out.println(person.getName());
	}
}
