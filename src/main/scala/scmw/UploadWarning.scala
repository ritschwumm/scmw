package scmw

sealed trait UploadWarning
case class UploadWarningWasDeleted(name:String)				extends UploadWarning
case class UploadWarningExists(name:String)					extends UploadWarning
case class UploadWarningDuplicate(names:Seq[String])			extends UploadWarning
case class UploadWarningDuplicateArchive(name:String)		extends UploadWarning
