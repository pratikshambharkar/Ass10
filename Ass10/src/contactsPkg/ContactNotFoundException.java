package contactsPkg;

@SuppressWarnings("serial")
public class ContactNotFoundException extends Exception {
	public ContactNotFoundException(String msg) {
		super(msg);
	}
}
