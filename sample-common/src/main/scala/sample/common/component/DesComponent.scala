package sample.common.component

import java.io.UnsupportedEncodingException
import java.nio.charset.StandardCharsets
import java.security.GeneralSecurityException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.Security

import helloscala.common.exception.HSInternalErrorException
import helloscala.common.exception.HSUnauthorizedException
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.util.encoders.Hex

class DesComponent(key: String) {
  private val TRIPLE_DES_TRANSFORMATION = "DESede/ECB/PKCS7Padding"
  private val ALGORITHM                 = "DESede"
  private val BOUNCY_CASTLE_PROVIDER    = "BC"
  private val UNICODE_FORMAT            = "UTF8"

  Security.addProvider(new BouncyCastleProvider)

  def encrypt(plainText: String, key: String): String = {
    val encryptedByte = encode(plainText.getBytes(StandardCharsets.UTF_8), key)
    Hex.toHexString(encryptedByte)
  }

  def encrypt(plainText: String): String = encrypt(plainText, key)

  def decrypt(cipherText: String, key: String): String = {
    val decryptedByte =
      decode(Hex.decode(cipherText.getBytes(StandardCharsets.UTF_8)), key)
    new String(decryptedByte)
  }

  def decrypt(cipherText: String): String =
    try {
      decrypt(cipherText, key)
    } catch {
      case e: Exception =>
        throw HSUnauthorizedException(s"解密失败，cipherText: $cipherText", cause = e)
    }

  private def encode(input: Array[Byte], key: String): Array[Byte] =
    try {
      val cipher =
        Cipher.getInstance(TRIPLE_DES_TRANSFORMATION, BOUNCY_CASTLE_PROVIDER)
      cipher.init(Cipher.ENCRYPT_MODE, buildKey(key.toCharArray))
      cipher.doFinal(input)
    } catch {
      case e @ (_: GeneralSecurityException | _: UnsupportedEncodingException) =>
        throw HSInternalErrorException("encode错误", cause = e)
    }

  private def decode(input: Array[Byte], key: String): Array[Byte] =
    try {
      val decrypter =
        Cipher.getInstance(TRIPLE_DES_TRANSFORMATION, BOUNCY_CASTLE_PROVIDER)
      decrypter.init(Cipher.DECRYPT_MODE, buildKey(key.toCharArray))
      decrypter.doFinal(input)
    } catch {
      case e @ (_: GeneralSecurityException | _: UnsupportedEncodingException) =>
        throw HSInternalErrorException("decode错误", cause = e)
    }

  @throws[NoSuchAlgorithmException]
  @throws[UnsupportedEncodingException]
  private def buildKey(password: Array[Char]): SecretKeySpec = {
    val digest = MessageDigest.getInstance("SHA-256")
    digest.update(String.valueOf(password).getBytes(UNICODE_FORMAT))
    val keys   = digest.digest
    val keyDes = java.util.Arrays.copyOf(keys, 24)
    new SecretKeySpec(keyDes, ALGORITHM)
  }

}
