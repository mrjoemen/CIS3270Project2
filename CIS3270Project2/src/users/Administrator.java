package users;

public class Administrator extends User {

	public Administrator(String firstName, String lastName, String id, String email, String address, int zip, String state, String userName,
			String password, String ssn, String securityQuestion) {
		super(firstName, lastName, id, email, address, zip, state, userName, password, ssn, securityQuestion);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void addFlight() {
		// do some code
	}
	@Override
	public void updateFlight() {
		// do some code
		
	}
	@Override
	public void deleteFlight() {
		// do some code
	}
	
	
	
}