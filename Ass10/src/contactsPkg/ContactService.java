package contactsPkg;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import databasePkg.Database;

public class ContactService {
	static List<Contact> contacts;

	public ContactService() {
		contacts = new ArrayList<Contact>();
	}

	void addContact(Contact contact, List<Contact> contacts) {
		contacts.add(contact);
	}

	void removeContact(Contact contact, List<Contact> contacts) throws ContactNotFoundException {
		if (contact == null)
			throw new ContactNotFoundException("No Contact found..");
		contacts.remove(contact);
		System.out.println("Contact removed successfully...");
		display(contacts);
	}

	List<Contact> searchContactByName(String name, List<Contact> contacts) throws ContactNotFoundException {
		List<Contact> foundContacts = new ArrayList<Contact>();
		for (Contact contact : contacts) {
			if (contact.getContactName().equalsIgnoreCase(name))
				foundContacts.add(contact);
		}
		if (foundContacts.isEmpty())
			throw new ContactNotFoundException("No Contact found..");
		return foundContacts;
	}

	List<Contact> SearchContactByNumber(String number, List<Contact> contacts) throws ContactNotFoundException {
		List<Contact> foundContacts = new ArrayList<Contact>();
		for (Contact contact : contacts) {
			for (String contactNum : contact.getContactNumber()) {
				if (contactNum.contains(number))
					foundContacts.add(contact);
			}
		}
		if (foundContacts.isEmpty())
			throw new ContactNotFoundException("No Contact found..");
		return foundContacts;
	}

	void addContactNumber(int contactID, String contactNumber, List<Contact> contacts) {
		String contactEmail = null; // default values
		String contactName = null; // default values
		Contact contact = new Contact(String.valueOf(contactID), contactName, contactEmail, contactNumber);
		contacts.add(contact);
		System.out.println("Contact added successfully ...");
		System.out.println(contact.toString());
	}

	void sortContactsByName(List<Contact> contacts) {
		Collections.sort(contacts, (o1, o2) -> {
			return o1.getContactName().compareTo(o2.getContactName());
		});
	}

	void serializeContactDetails(List<Contact> contacts, String filename) throws IOException {
		FileOutputStream fos = new FileOutputStream(filename);
		try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(contacts);
			System.out.println("Contacts Serialized check file...");
		}
	}

	List<Contact> deserializeContact(String filename) throws IOException, ClassNotFoundException {
		FileInputStream fis = new FileInputStream(filename);
		try (ObjectInputStream ois = new ObjectInputStream(fis)) {
			@SuppressWarnings("unchecked")
			List<Contact> readObject = (List<Contact>) ois.readObject();
			;
			List<Contact> contacts = readObject;
			return contacts;
		}
	}

	void readContactsFromFile(List<Contact> contacts, String fileName) throws FileNotFoundException {
		FileInputStream fis = new FileInputStream(fileName);
		Scanner sc = new Scanner(fis);
		while (sc.hasNextLine()) {
			String data[] = sc.nextLine().split(",");
			String contactID = data[0];
			String contactName = data[1];
			String contactEmail = data[2];
			String contactNumber = "";

			if (data.length > 3)
				for (int i = 3; i < data.length; i++)
					contactNumber += data[i] + ",";

			Contact contact = new Contact(contactID, contactName, contactEmail, contactNumber);
			contacts.add(contact);
		}

		System.out.println("Contacts read successfully");
		sc.close();
	}

	Set<Contact> populateContactFromDb() throws ClassNotFoundException, SQLException {
		Database dbConnection = new Database();
		String query = "Select contactId, contactName, contactEmail, contactList from contact_tbl";
		ResultSet rs = Database.st.executeQuery(query);

		Set<Contact> contacts = new LinkedHashSet<>();
		while (rs.next()) {
			String contactID = rs.getString("contactId");
			String contactName = rs.getString("contactName");
			String contactEmail = rs.getString("contactEmail");
			String contactNumber = rs.getString("contactList");

			Contact contact = new Contact(contactID, contactName, contactEmail, contactNumber);
			contacts.add(contact);
		}

		dbConnection.close();
		return contacts;
	}

	Contact createContact() {
		@SuppressWarnings("resource") // if we close scanner at the end would not ask for y/n in while
		Scanner sc = new Scanner(System.in);
		String contactNumber, contactEmail, contactName, contactID;
		System.out.println("Enter contactID ");
		contactID = sc.nextLine();
		System.out.println("Enter contactName");
		contactName = sc.nextLine();
		System.out.println("Enter contactEmail");
		contactEmail = sc.nextLine();
		System.out.println("Enter contactNumber");
		contactNumber = sc.nextLine();
		Contact contact = new Contact(contactID, contactName, contactEmail, contactNumber);
		return contact;
	}

	Contact getContact() {
		@SuppressWarnings("resource") // if closes sc at the end would not ask for y/n in while
		Scanner sc = new Scanner(System.in);

		if (contacts.isEmpty()) {
			System.out.println("No contacts saved plz save some contact first..");
			return null;
		}

		System.out.println("Saved contacts are....\n");
		display(contacts);
		System.out.println("Enter enter contact id to remove contact ");
		int contactID = Integer.parseInt(sc.nextLine());
		for (Contact contact : contacts) {
			if (contact.getContactID() == contactID)
				return contact;
		}
		return null;
	}

	void display(Collection<Contact> contacts) {
		for (Contact contact : contacts) {
			System.out.println(contact.toString());
		}
	}

	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in);
		ContactService cs = new ContactService();
		String fileName = System.getProperty("user.dir") + "\\Contact.txt";
		String serFileName = System.getProperty("user.dir") + "\\SerializedContacts.txt";
		while (true) {
			System.out.println("0.DISPLAY CONTACTS");
			System.out.println("1.Read Contacts From File");
			System.out.println("2.Add contacts");
			System.out.println("3.Remove contact");
			System.out.println("4.Search Contact By Name");
			System.out.println("5.Search Contact By Number");
			System.out.println("6.Add Contact Number");
			System.out.println("7.Sort Contacts By Name");
			System.out.println("8.Serialize Contact Details");
			System.out.println("9.Deserialize Contact");
			System.out.println("10.Populate Contact From Db");

			int choice = Integer.parseInt(sc.nextLine());
			switch (choice) {
			case 0:
				cs.display(contacts);
				break;
			case 1:
				cs.readContactsFromFile(contacts, fileName);
				cs.display(contacts);
				break;
			case 2:
				Contact newcontact = cs.createContact();
				cs.addContact(newcontact, contacts);
				break;
			case 3:
				try {
					Contact uselessContact = cs.getContact();
					cs.removeContact(uselessContact, contacts);
				} catch (Exception e) {
					System.out.println(e);
				}
				break;
			case 4:
				System.out.println("Enter a name ");
				String name = sc.nextLine();
				try {
					List<Contact> contactNames = cs.searchContactByName(name, contacts);
					cs.display(contactNames);
				} catch (Exception e) {
					System.out.println(e);
				}
				break;
			case 5:
				System.out.println("Enter a number ");
				String number = sc.nextLine();
				try {
					List<Contact> contactNumber = cs.SearchContactByNumber(number, contacts);
					cs.display(contactNumber);
				} catch (Exception e) {
					System.out.println(e);
				}
				break;
			case 6:
				System.out.println("Enter contactId");
				int contactId = Integer.parseInt(sc.nextLine());
				System.out.println("Enter contactNo");
				String contactNo = sc.nextLine();
				cs.addContactNumber(contactId, contactNo, contacts);
				break;
			case 7:
				cs.sortContactsByName(contacts);
				System.out.println("Sorted Contacts : ");
				cs.display(contacts);
				break;
			case 8:
				cs.serializeContactDetails(contacts, serFileName);
				break;
			case 9:
				List<Contact> desContacts = cs.deserializeContact(serFileName);
				System.out.println("Serialized contacts...");
				cs.display(desContacts);
				break;
			case 10:
				Set<Contact> dbContacts = cs.populateContactFromDb();
				System.out.println("Contacts From database : ");
				cs.display(dbContacts);
				contacts.addAll(dbContacts); // merging file contacts and db contacts
				break;
			}

			System.out.println("Do you want to continue Y/N");
			char c = sc.nextLine().toLowerCase().charAt(0);
			if (c == 'n')
				break; // breaks while loop
		}
		sc.close();
	}

}
