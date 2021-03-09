package contactsPkg;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Contact implements Serializable{
	
	private int contactID;
	private String contactName;
	private String contactEmail;
	private ArrayList<String> contactNumber;
	
	
	public Contact(String contactID, String contactName, String contactEmail, String contactNumber) {
		this.setContactID(contactID);
		this.setContactName(contactName);
		this.setContactEmail(contactEmail);
		this.setContactNumber(contactNumber);
	}
	
	private void setContactID(String contactID) {
		this.contactID = Integer.parseInt(contactID);
	}
	private void setContactName(String contactName) {
		this.contactName = contactName;
	}
	private void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}
	private void setContactNumber(String contactNumber) {
		if(contactNumber==null)//Handles error while getting data from database java.lang.NullPointerException
			contactNumber = "";
		
		ArrayList<String> contacts = new ArrayList<>();
		String data[] = contactNumber.split(",");
		for (int i = 0; i < data.length; i++)
			contacts.add(data[i]);
		this.contactNumber = contacts;
	}
	
	public int getContactID() {
		return contactID;
	}
	public String getContactName() {
		return contactName;
	}
	public String getContactEmail() {
		return contactEmail;
	}
	public ArrayList<String> getContactNumber() {
		return contactNumber;
	}

	@Override
	public String toString() {
		return "Contact [contactID=" + contactID + ", contactName=" + contactName + ", contactEmail=" + contactEmail
				+ ", contactNumber=" + contactNumber + "]";
	}
	
	
}
