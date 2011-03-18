package scmw

/** outcome of API#edit and API#upload */
sealed trait UploadResult
case class UploadSuccess(fileName:String, pageTitle:String)	extends UploadResult
case class UploadFailure(failureCode:String)				extends UploadResult
case class UploadError(errorCode:String)					extends UploadResult
case class UploadAborted(warnings:Set[UploadWarning])		extends UploadResult
