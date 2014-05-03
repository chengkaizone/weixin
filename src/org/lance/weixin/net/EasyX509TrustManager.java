package org.lance.weixin.net;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class EasyX509TrustManager implements X509TrustManager {

	private X509TrustManager standardTrustManager = null;

	public EasyX509TrustManager(KeyStore keystore)
			throws NoSuchAlgorithmException, KeyStoreException {
		super();
		TrustManagerFactory factory = TrustManagerFactory
				.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		factory.init(keystore);
		TrustManager[] trustmanagers = factory.getTrustManagers();
		if (trustmanagers.length == 0) {
			throw new NoSuchAlgorithmException("no trust manager found");
		}
		this.standardTrustManager = (X509TrustManager) trustmanagers[0];
	}

	public void checkClientTrusted(java.security.cert.X509Certificate[] certificates,
			String authType) throws java.security.cert.CertificateException {
		standardTrustManager.checkClientTrusted(certificates, authType);
	}

	public java.security.cert.X509Certificate[] getAcceptedIssuers() {
		return this.standardTrustManager.getAcceptedIssuers();
	}

	@Override
	public void checkServerTrusted(java.security.cert.X509Certificate[] certificates,
			String authType) throws java.security.cert.CertificateException {
		if ((certificates != null) && (certificates.length == 1)) {
			certificates[0].checkValidity();
		} else {
			standardTrustManager.checkServerTrusted(certificates, authType);
		}
	}
}
