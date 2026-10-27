package hu.beadando;

import com.oanda.v20.account.AccountID;

/** Oanda beállítások – demó (practice) számla, a futtatható jar miatt beégetve. */
public class Config {
	private Config() {}
	public static final String URL = "https://api-fxpractice.oanda.com";
	// // Ha be van állítva OANDA_TOKEN / OANDA_ACCOUNT környezeti változó:
	public static final String TOKEN = env("OANDA_TOKEN", "70c5c0809027e12bb11210e51e7a5c0a-7b46af1e44e87e2884692822d96e3906");
	public static final AccountID ACCOUNTID = new AccountID(env("OANDA_ACCOUNT", "101-004-40679900-001"));

	private static String env(String name, String def) {
		String v = System.getenv(name);
		return (v == null || v.isBlank()) ? def : v;
	}
}
