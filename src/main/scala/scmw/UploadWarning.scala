package scmw

import scutil.lang.ISeq

sealed trait UploadWarning
case class UploadWarningWasDeleted(name:String)				extends UploadWarning
case class UploadWarningExists(name:String)					extends UploadWarning
// BETTER use a Nes
case class UploadWarningDuplicate(names:ISeq[String])		extends UploadWarning
case class UploadWarningDuplicateArchive(name:String)		extends UploadWarning
