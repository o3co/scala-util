package jp.o3co.http

import spray.http.StatusCode

/**
 * Fundamental Http Response for AnyVal
 */
case class StatusMessage(code: StatusCode, value: Any)
