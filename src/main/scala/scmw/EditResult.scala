package scmw

/** outcome of API#edit and API#newsection */
sealed trait EditResult
case class EditSuccess(pageTitle:String)		extends EditResult
case class EditFailure(failureCode:String)		extends EditResult
case class EditError(errorCode:String)			extends EditResult
case object EditAborted							extends EditResult
