package freecycling.example.freecycling.security;

public class WrongUserException extends Exception {
	public WrongUserException() {
		super("User is not allowed to perform this action");
	}
}