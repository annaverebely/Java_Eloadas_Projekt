package hu.beadando;

import com.oanda.v20.account.AccountID;

/** Oanda beállítások. Éles adatot NE commitoljatok publikus repóba! */
public class Config {
	private Config() {}
	public static final String URL = "https://api-fxpractice.oanda.com";
	// Környezeti változóból (OANDA_TOKEN, OANDA_ACCOUNT) olvas, ha nincs, az alapérték él:
	public static final String TOKEN = env("OANDA_TOKEN", "<TOKEN>");
	public static final AccountID ACCOUNTID = new AccountID(env("OANDA_ACCOUNT", "<ACCOUNT_ID>"));

	private static String env(String name, String def) {
		String v = System.getenv(name);
		return (v == null || v.isBlank()) ? def : v;
	}
}
