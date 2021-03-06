package cz.metacentrum.perun.spRegistration.service.exceptions;

/**
 * Exception thrown when some internal error (database inconsistency...) occurs.
 *
 * @author Dominik Frantisek Bucik &lt;bucik@ics.muni.cz&gt;
 */
public class InternalErrorException extends Exception {

	public InternalErrorException() {
		super();
	}

	public InternalErrorException(String s) {
		super(s);
	}

	public InternalErrorException(String s, Throwable throwable) {
		super(s, throwable);
	}

	public InternalErrorException(Throwable throwable) {
		super(throwable);
	}

	protected InternalErrorException(String s, Throwable throwable, boolean b, boolean b1) {
		super(s, throwable, b, b1);
	}
}
