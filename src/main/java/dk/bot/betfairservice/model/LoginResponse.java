package dk.bot.betfairservice.model;

/**Response of login operation*/
public class LoginResponse {

	/**If true then login operation was finished successfully.*/
	private final boolean success;
	
	private final String apiStatusCode;
	private final String loginStatusCode;
	private final String exceptionMessage;
	
	public LoginResponse(boolean success,String apiStatusCode,String loginStatusCode, String exceptionMessage) {
		this.success=success;
		this.apiStatusCode=apiStatusCode;
		this.loginStatusCode=loginStatusCode;
		this.exceptionMessage=exceptionMessage;
	}

	public boolean isSuccess() {
		return success;
	}

	public String getApiStatusCode() {
		return apiStatusCode;
	}

	public String getLoginStatusCode() {
		return loginStatusCode;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}	
}
