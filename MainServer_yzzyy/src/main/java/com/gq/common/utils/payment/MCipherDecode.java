package com.gq.common.utils.payment;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class MCipherDecode {

	static {
		if (Security.getProvider("BC") == null) {

			Security.addProvider(new BouncyCastleProvider());
		}
	}

	private String encryptKey = "12345678";

	public MCipherDecode(String key) {
		encryptKey = key.substring(0, 8);
	}

	public String getEncryptKey() {
		return encryptKey;
	}

	public void setEncryptKey(String encryptKey) {
		this.encryptKey = encryptKey.substring(0, 8);
	}

	private static byte[] getSrcBytes(byte[] srcBytes, byte[] wrapKey) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException, ShortBufferException, IllegalBlockSizeException,
			BadPaddingException, NoSuchProviderException, InvalidAlgorithmParameterException {
		SecretKeySpec key = new SecretKeySpec(wrapKey, "DES");

		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding", "BC");

		cipher.init(Cipher.DECRYPT_MODE, key);

		byte[] cipherText = cipher.doFinal(srcBytes);

		return cipherText;
	}

	public static byte[] DecodeBase64String(String base64Src) throws IOException {
		BASE64Decoder de = new BASE64Decoder();
		byte[] base64Result = de.decodeBuffer(base64Src);
		return base64Result;

	}

	public String getDecodeString(String urlString) throws IOException, InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, ShortBufferException, IllegalBlockSizeException, BadPaddingException,
			NoSuchProviderException, InvalidAlgorithmParameterException {
		String tempString = URLDecoder.decode(urlString, "ISO-8859-1");
		String basedString = tempString.replaceAll(",", "+");
		byte[] tempBytes = DecodeBase64String(basedString);
		byte[] tempSrcBytes = getSrcBytes(tempBytes, encryptKey.getBytes("ISO-8859-1"));
		return new String(tempSrcBytes, "ISO-8859-1");

	}

}
