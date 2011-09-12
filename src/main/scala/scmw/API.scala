package scmw

import java.io.File

import scutil.log.Logging
import scutil.ext.OptionImplicits._
import scutil.ext.BooleanImplicits._

import scjson._
import scjson.JSNavigation._

import scmw.web._

final class API(apiURL:String, enableWrite:Boolean) extends Logging {
	val connection	= new Connection(apiURL)
	
	//------------------------------------------------------------------------------
	
	/** login a user with a given password */
	def login(user:String, password:String):LoginResult = {
		if (!enableWrite)	{
			DEBUG("api#login", "user=", user)
			return LoginSuccess(user)
		}
		
		val	req1	= Seq(
			"action"		-> "login",
			"format"		-> "json",
			"lgname"		-> user,
			"lgpassword"	-> password
		)
		val res1	= connection POST req1
		require(res1.nonEmpty, "no json result")
		errorCode(res1) foreach { code => return LoginError(code) }
		
		val login	= res1	/ "login"
		val token	= login	/ "token"		string
		val outUser	= login	/ "lgusername"	string
		
		resultCode(login) match {
			case Some("NeedToken")	=>	// handled later
			case Some("Success")	=> return LoginSuccess(outUser getOrError "expected a username")
			case Some(code)			=> return LoginFailure(code)
			case None				=> sys error ("expected a result")
		}
		
		val req2	= req1 ++ optionally("lgtoken" -> token)
		val res2	= connection POST req2
		require(res2.nonEmpty,	"no json result")
		errorCode(res2) foreach { code => return LoginError(code) }
		
		val login2		= res2		/ "login"
		val outUser2	= login2	/ "lgusername"	string
		
		resultCode(login2) match {
			case Some("Success")	=> LoginSuccess(outUser2 getOrError "expected a username")
			case Some(code)			=> LoginFailure(code)
			case _					=> sys error ("expected a result")
		}
	}
	
	/** logout a user */
	def logout() {
		if (!enableWrite) {
			DEBUG("api#logout")
			return
		}
		
		val	req	= Seq(
			"action"	-> "logout",
			"format"	-> "json"
		)
		val res	= connection POST req
		require(res.nonEmpty,	"no json result")
	}
	
	/** simplified edit method to append a new section to a page */
	def newsection(title:String, summary:String, text:String):EditResult = {
		if (!enableWrite) {
			DEBUG("api#newsection", "title=", title, "summary=", summary, "text=", text)
			return EditSuccess(title)
		}
		
		val	req1	= Seq(
			"action"	-> "query",
			"format"	-> "json",
			"prop"		-> "info|revisions",
			"intoken"	-> "edit",	// provides edittoken and starttimestamp
			"rvprop"	-> "timestamp",
			"titles"	-> title
		)
		val res1			= connection POST req1
		require(res1.nonEmpty,	"no json result")
		errorCode(res1) foreach { code => return EditError(code) }
		
		val page			= res1		/ "query" / "pages"	first
		val edittoken		= page		/ "edittoken"		string
		val starttimestamp	= page		/ "starttimestamp"	string
		val revision		= page		/ "revisions"		first
		val basetimestamp	= revision	/ "timestamp"		string
		//val missing			= page / "missing" isDefined
		
		val	req2	= Seq(
			"action"	-> "edit",
			"format"	-> "json",
			"title"		-> title,
			"summary"	-> summary,
			"text"		-> text,
			"section"	-> "new"	// hardcoded
		) ++ optionally(
			"token"				-> edittoken,
			"basetimestamp"		-> basetimestamp,
			"starttimestamp"	-> starttimestamp
		)
		val res2	= connection POST req2
		require(res2.nonEmpty,	"no json result")
		errorCode(res2) foreach { code => return EditError(code) }
		
		val edit		= res2 / "edit"
		val outTitle	= edit / "title" string
		
		resultCode(edit) match {
			case Some("Success")	=> EditSuccess(outTitle getOrError "expected a title")
			case Some(code)			=> EditFailure(code)
			case _					=> sys error ("expected a result")
		}
	}
	
	/** edit a page with an editor function, if it returns None editing is aborted */
	def edit(title:String, summary:String, section:Option[Int], change:String=>Option[String]):EditResult = {
		if (!enableWrite) {
			DEBUG("api#edit", "title=", title, "section=", section, "summary=", summary, "change=", change)
			return EditSuccess(title)
		}
		
		val sectionString	= section map {_.toString}
		
		val	req1	= Seq(
			"action"	-> "query",
			"format"	-> "json",
			"prop"		-> "info|revisions",
			"intoken"	-> "edit",	// provides edittoken and starttimestamp
			"rvprop"	-> "timestamp|content",
			"titles"	-> title
		) ++
		optionally(
			"rvsection"	-> sectionString
		)
		val res1			= connection POST req1
		require(res1.nonEmpty,	"no json result")
		errorCode(res1) foreach { code => return EditError(code) }
		
		val page			= res1 		/ "query" / "pages"	first
		val edittoken		= page 		/ "edittoken"		string
		val starttimestamp	= page		/ "starttimestamp"	string
		val revision		= page		/ "revisions"		first
		val basetimestamp	= revision	/ "timestamp"		string
		val content			= revision	/ "*"				string
		val missing			= page		/ "missing"			string
		
		val original	= content orElse (missing map { _ => "" }) 
		val changed		= original flatMap change
		if (changed.isEmpty)	return EditAborted
		val changed1	= changed getOrError "no text???"
		
		val	req2	= Seq(
			"action"	-> "edit",
			"format"	-> "json",
			"title"		-> title,
			"summary"	-> summary,
			"text"		-> changed1
		) ++ optionally(
			"token"				-> edittoken,
			"basetimestamp"		-> basetimestamp,
			"starttimestamp"	-> starttimestamp,
			"section"			-> sectionString
		)
		val res2	= connection POST req2
		require(res2.nonEmpty,	"no json result")
		errorCode(res2) foreach { code => return EditError(code) }
		
		// TODO handle edit conflicts
		val edit		= res2 / "edit"
		val outTitle	= edit / "title" string
		
		resultCode(edit) match {
			case Some("Success")	=> EditSuccess(outTitle getOrError "expected a title")
			case Some(code)			=> EditFailure(code)
			case _					=> sys error ("expected a result")
		}
	}	
	
	/** upload a file */
	def upload(filename:String, summary:String, text:String, watch:Boolean, file:File, callback:UploadCallback):UploadResult = {
		if (!enableWrite) {
			DEBUG("api#upload", "filename=", filename, "summary=", summary, "text=", text, "watch=", watch, "file=", file, "callback=", callback)
			return UploadSuccess(filename, Namespace.file(filename))
		}
		
		val watchString	= watch guard "true"
		
		val	req1	= Seq(
			"action"	-> "query",
			"format"	-> "json",
			"prop"		-> "info",
			"intoken"	-> "edit",
			"titles"	-> (Namespace file filename)
		)
		
		val res1			= connection POST req1
		require(res1.nonEmpty,	"no json result")
		errorCode(res1) foreach { code => return UploadError(code) }
		
		val page			= res1 / "query" / "pages"	first
		val edittoken		= page / "edittoken"		string
		
		val req2	= Seq(
			"action"	-> "upload",
			"format"	-> "json",
			"filename"	-> filename,
			"comment"	-> summary,
			"text"		-> text
			//	ignorewarnings	url	sessionkey
		) ++ optionally(
			"watch"	-> watchString,	// TODO deprecated
			"token"	-> edittoken
		)
			
		// NOTE either 'sessionkey', 'file', 'url'
		val res2	= connection POST_multipart (req2, "file", file, callback.progress)
		require(res2.nonEmpty,	"no json result")
		errorCode(res2) foreach { code => return UploadError(code) }
		
		val upload		= res2		/ "upload"
		val outName		= upload	/ "filename"	string
		val sessionkey	= upload	/ "sessionkey"	string	
		val warnings	= upload	/ "warnings"
		
		resultCode(upload) match {
			case Some("Success")	=> 
				val	name	= outName getOrError "expected filename"
				val	title	= Namespace file name
				return UploadSuccess(name, title)
			case Some("Warning")	=> // handled later
			case Some(code)			=> return UploadFailure(code)
			case _					=> sys error ("expected a result")
		}
		
		// TODO handle more warnings with messages
		val warningWasDeleted		= (warnings / "was-deleted"			string)		map UploadWarningWasDeleted.apply
		val warningExists			= (warnings / "exists"				string)		map	UploadWarningExists.apply     
		val warningDuplicate		= (warnings / "duplicate"			arraySeq)	map { _ flatMap { _.string } } map UploadWarningDuplicate.apply
		val warningDuplicateArchive	= (warnings / "duplicate-archive"	string)		map UploadWarningDuplicateArchive.apply
		val ignorableWarnings:Set[UploadWarning]	= Set(warningWasDeleted, warningExists, warningDuplicate, warningDuplicateArchive).flatten
		if (ignorableWarnings.nonEmpty && !(callback ignore ignorableWarnings))	 return UploadAborted(ignorableWarnings)
		
		// handle other warnings
		val ignoredWarningKeys			= Set("was-deleted", "exists",	"duplicate", "duplicate-archive", "large-file")
		val allWarningKeys:Set[String]	= (warnings.objectMap map { _.keys map { _.value } }).toSet.flatten
		val relevantWarnings			= allWarningKeys -- ignoredWarningKeys
		if (relevantWarnings.nonEmpty)	return UploadFailure(relevantWarnings mkString ", ")
		
		require(sessionkey.isDefined, "to resume after warnings, a sessionkey is required")
		
		val req3	= Seq(
			"action"			-> "upload",
			"format"			-> "json",
			"filename"			-> filename,
			"comment"			-> summary,
			"text"				-> text,
			"ignorewarnings"	-> "true"
			// url
		) ++ optionally(
			"watch"			-> watchString,	// TODO deprecated
			"token"			-> edittoken,	
			"sessionkey"	-> (sessionkey map { _.toString })
		)
		
		val res3	= connection POST req3
		require(res3.nonEmpty,	"no json result")
		errorCode(res3) foreach { code => return UploadError(code) }
		
		val upload2		= res3		/ "upload"
		val outName2	= upload2	/ "filename" string
		
		resultCode(upload2) match {
			case Some("Success")	=> 
				val	name	= outName2 getOrError "expected filename"
				val	title	= Namespace file name
				// NOTE api.php does not write the description page if it's not the initial upload
				if (warningExists.isDefined) {
					val	editResult	= edit(title, "overwritten", None, Function const Some(text))
					editResult match {
						case EditFailure(code) =>
							ERROR("could not change overwritten file description (failure)", title, code)
						case EditError(code) =>
							ERROR("could not change overwritten file description (error)", title, code)
						case _ =>
							// ok
					}
				}
				return UploadSuccess(name, title)
			case Some(code)			=> return UploadFailure(code)
			case _					=> sys error ("expected a result")
		}
	}
	
	//------------------------------------------------------------------------------
	
	private def resultCode(response:Option[JSValue]):Option[String] = 
			response / "result" string  
			
	private def errorCode(response:Option[JSValue]):Option[String] = { 
		val	error	= response / "error"
		error foreach { it => ERROR(JSMarshaller apply it) }
		error / "code" string
	}
			
	/** helper function for optional request parameters */
	private def optionally(values:Pair[String,Option[String]]*):List[Pair[String,String]] =
			values.toList flatMap optionally1
			
	private def optionally1(value:Pair[String,Option[String]]):Option[Pair[String,String]] = value match {
		case Pair(key, Some(value))	=> Some(Pair(key, value))
		case Pair(key, None)		=> None
	}
}
