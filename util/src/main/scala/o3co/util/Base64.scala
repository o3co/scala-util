package o3co.util

import java.util.Base64


sealed trait Base64Encode
object Base64Encode {
  case object BASIC extends Base64Encode 
  case object URL   extends Base64Encode 
  case object MIME  extends Base64Encode 
  def RFC2045 = MIME 

  /**
   * Encode value with Base64Basic
   */
  def encode(bytes: Array[Byte], padding: Boolean = true) = 
    Base64Encoded(
      (padding match {
        case true  => Base64.getEncoder()
        case false => Base64.getEncoder().withoutPadding()
      }).encodeToString(bytes), 
      BASIC, 
      padding
    )


  def encodeURL(bytes: Array[Byte], padding: Boolean = false): Base64Encoded = 
    Base64Encoded(
      (padding match {
        case true => Base64.getUrlEncoder()
        case false => Base64.getUrlEncoder().withoutPadding()
      }).encodeToString(bytes), 
      URL, 
      padding
    )

  def encodeMIME(bytes: Array[Byte], padding: Boolean = true): Base64Encoded =  
    Base64Encoded(
      (padding match {
        case true  => Base64.getMimeEncoder()
        case false => Base64.getMimeEncoder().withoutPadding()
      }).encodeToString(bytes), 
      MIME, 
      padding
    )
}

case class Base64Encoded(encoded: String, encode: Base64Encode = Base64Encode.URL, padding: Boolean = true) {

  lazy val decode: Array[Byte] = encode match {
    case Base64Encode.BASIC => 
      Base64.getDecoder().decode(encoded)
    case Base64Encode.URL   => 
      Base64.getUrlDecoder().decode(encoded)
    case Base64Encode.MIME  => 
      Base64.getMimeDecoder().decode(encoded)
  }

  override def toString: String = encoded
}

object Base64Encoded {
}

/*
 */
object Base64Basic {

  /**
   * {{{
   *   Base64Basic(encoded)
   * }}}
   */
  def apply(value: String, padding: Boolean = true) = 
    Base64Encoded(value, Base64Encode.BASIC, padding) 

  /**
   * {{{
   *   Base64Basic.withPadding(encoded)
   * }}}
   */
  def withPadding(value: String) = 
    apply(value, true)

  /**
   * {{{
   *   Base64Basic.withoutPadding(encoded)
   * }}}
   */
  def withoutPadding(value: String) = 
    apply(value, false)
}

/*
 */
object Base64URL {

  /**
   * {{{
   *   Base64URL(encoded)
   * }}}
   */
  def apply(value: String, padding: Boolean = false) = 
    Base64Encoded(value, Base64Encode.URL, padding)

  /**
   * {{{
   *   Base64URL.withPadding(encoded)
   * }}}
   */
  def withPadding(value: String) = 
    apply(value, true)

  /**
   * {{{
   *   Base64URL.withoutPadding(encoded)
   * }}}
   */
  def withoutPadding(value: String) = 
    apply(value, false)
}

