package scmw

/** outcome of API#login */
sealed trait LoginResult
case class LoginSuccess(userName:String)		extends LoginResult
case class LoginFailure(failureCode:String)		extends LoginResult
case class LoginError(errorCode:String)			extends LoginResult
