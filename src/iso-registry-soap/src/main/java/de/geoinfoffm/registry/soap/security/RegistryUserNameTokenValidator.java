/**
 * Copyright (c) 2014, German Federal Agency for Cartography and Geodesy
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions 
 * are met:
 *     * Redistributions of source code must retain the above copyright
 *     	 notice, this list of conditions and the following disclaimer.
 
 *     * Redistributions in binary form must reproduce the above 
 *     	 copyright notice, this list of conditions and the following 
 *       disclaimer in the documentation and/or other materials 
 *       provided with the distribution.
 
 *     * The names "German Federal Agency for Cartography and Geodesy", 
 *       "Bundesamt für Kartographie und Geodäsie", "BKG", "GDI-DE", 
 *       "GDI-DE Registry" and the names of other contributors must not 
 *       be used to endorse or promote products derived from this 
 *       software without specific prior written permission.
 *       
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE GERMAN 
 * FEDERAL AGENCY FOR CARTOGRAPHY AND GEODESY BE LIABLE FOR ANY DIRECT, 
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) 
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF 
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.geoinfoffm.registry.soap.security;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.ws.security.WSPasswordCallback;
import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.handler.RequestData;
import org.apache.ws.security.message.token.UsernameToken;
import org.apache.ws.security.validate.UsernameTokenValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.codec.Base64;

import de.geoinfoffm.registry.core.BCryptHashingStrategy;
import de.geoinfoffm.registry.core.configuration.RegistryConfiguration;

/**
 * Token validator for SOAP client that checks if the SOAP header contains a
 * valid username/password combination or a correctly encrypted token
 * (e.g. for use by monitoring tool).
 * 
 * @author Florian Esser
 * 
 */
public class RegistryUserNameTokenValidator extends UsernameTokenValidator
{
	private static final Logger log = LoggerFactory.getLogger(RegistryUserNameTokenValidator.class);

	private BCryptHashingStrategy bcrypt = new BCryptHashingStrategy();

	private RegistryConfiguration registryConfiguration;

	public RegistryUserNameTokenValidator(RegistryConfiguration registryConfiguration) {
		this.registryConfiguration = registryConfiguration;
	}

	@Override
	protected void verifyPlaintextPassword(UsernameToken usernameToken, RequestData data) throws WSSecurityException {
		if (data.getCallbackHandler() == null) {
			throw new WSSecurityException(WSSecurityException.FAILURE, "noCallback");
		}

		String user = usernameToken.getName();
		String password = usernameToken.getPassword();
		String nonce = usernameToken.getNonce();
		String createdTime = usernameToken.getCreated();
		String pwType = usernameToken.getPasswordType();
		boolean passwordsAreEncoded = usernameToken.getPasswordsAreEncoded();

		WSPasswordCallback pwCb = new WSPasswordCallback(user, null, pwType, WSPasswordCallback.USERNAME_TOKEN, data);
		try {
			data.getCallbackHandler().handle(new Callback[] { pwCb });
		}
		catch (IOException e) {
			if (log.isDebugEnabled()) {
				log.debug(e.getMessage(), e);
			}
			throw new WSSecurityException(WSSecurityException.FAILED_AUTHENTICATION, null, null, e);
		}
		catch (UnsupportedCallbackException e) {
			if (log.isDebugEnabled()) {
				log.debug(e.getMessage(), e);
			}
			throw new WSSecurityException(WSSecurityException.FAILED_AUTHENTICATION, null, null, e);
		}
		String origPassword = pwCb.getPassword();
		if (origPassword == null) {
			if (log.isDebugEnabled()) {
				log.debug("Callback supplied no password for: " + user);
			}
			throw new WSSecurityException(WSSecurityException.FAILED_AUTHENTICATION);
		}
		if (usernameToken.isHashed()) {
			String passDigest;
			if (passwordsAreEncoded) {
				passDigest = UsernameToken.doPasswordDigest(nonce, createdTime, Base64.decode(origPassword.getBytes()));
			}
			else {
				passDigest = UsernameToken.doPasswordDigest(nonce, createdTime, origPassword);
			}
			if (!passDigest.equals(password)) {
				throw new WSSecurityException(WSSecurityException.FAILED_AUTHENTICATION);
			}
		}
		else {
			if (!bcrypt.matches(password, origPassword) /*&& !isValidToken(user, password)*/) {
				throw new WSSecurityException(WSSecurityException.FAILED_AUTHENTICATION);
			}
		}
	}

//	private boolean isValidToken(String emailAddress, String token) {
//		if (StringUtils.isEmpty(emailAddress) || StringUtils.isEmpty(token)) {
//			return false;
//		}
//		
//		String presharedKey = registryConfiguration.getMonitoringKey();
//		
//		AesEncryptionTool aes = new AesEncryptionTool(presharedKey);
//		return emailAddress.equals(aes.decrypt(token));
//	}

//	private String decrypt(byte[] cipherText, byte[] salt, String password) throws UnsupportedEncodingException, IllegalBlockSizeException,
//			BadPaddingException, InvalidParameterSpecException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
//			InvalidAlgorithmParameterException, InvalidKeySpecException {
//
//		/* Derive the key, given password and salt. */
//		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
//		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
//		SecretKey tmp = factory.generateSecret(spec);
//		SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
//
//		/* Decrypt the message, given derived key and initialization vector. */
//		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//		AlgorithmParameters params = cipher.getParameters();
//		byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
//		cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
//		String plaintext = new String(cipher.doFinal(cipherText), "UTF-8");
//
//		return plaintext;
//	}

//	public String decrypt(String key, String encrypted) {
//		try {
//			Key k = new SecretKeySpec(Base64.decode(key.getBytes()), "AES");
//			Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
//			c.init(Cipher.DECRYPT_MODE, k);
//			byte[] decodedValue = Base64.decode(encrypted.getBytes("UTF-8"));
//			byte[] decValue = c.doFinal(decodedValue);
//			String decryptedValue = new String(decValue);
//			return decryptedValue;
//		}
//		catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
//			new Object(); 
//		}
//
//		return null;
//	}
}
