package mc.alk.arena.objects.Exceptions;

public class NeverWouldJoinException extends InvalidEventException{
	private static final long serialVersionUID = 1L;

	public NeverWouldJoinException(String msg) {
		super(msg);
	}
}
