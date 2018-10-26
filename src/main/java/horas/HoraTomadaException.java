package horas;

import horas.pojo.Calendariosemanal;
import horas.pojo.ProblemaHora;

public class HoraTomadaException extends Exception {

	private static final long serialVersionUID = 2017399410767766406L;

	public HoraTomadaException() {
		// TODO Auto-generated constructor stub
	}

	public HoraTomadaException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public HoraTomadaException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public HoraTomadaException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public HoraTomadaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}
	
	private ProblemaHora problema;
	private Calendariosemanal calendarioActualizado;

	public ProblemaHora getProblema() {
		return problema;
	}

	public void setProblema(ProblemaHora problema) {
		this.problema = problema;
	}

	public Calendariosemanal getCalendarioActualizado() {
		return calendarioActualizado;
	}

	public void setCalendarioActualizado(Calendariosemanal calendarioActualizado) {
		this.calendarioActualizado = calendarioActualizado;
	}
	
	

}
