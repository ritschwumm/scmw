package scmw.web

import java.net.URI

import scutil.ext.AnyRefImplicits._

/*
[scheme:][//authority][path][?query][#fragment] 
[user-info@]host[:port] 
*/
object URIData {
	def parse(str:String):URIData = parse(new URI(str))
	def parse(uri:URI):URIData = new URIData(
		uri.getScheme.nullOption,
		uri.getAuthority.nullOption,
		uri.getPath.nullOption,
		uri.getQuery.nullOption,
		uri.getFragment.nullOption,
		uri.getUserInfo.nullOption,
		uri.getHost.nullOption,
		uri.getPort match { case -1 => None; case x => Some(x) }
	)
}

case class URIData(
	scheme:Option[String],
	authority:Option[String],
	path:Option[String],
	query:Option[String],
	fragment:Option[String],
	userInfo:Option[String],
	host:Option[String],
	port:Option[Int]
) {
	def target:Option[Target]	=
			for {
				sch	<- scheme
				hst	<- host
				prt	<- port orElse defaultPort
			} 
			yield Target(hst, prt)
			
	def cred:Option[Cred]	=
			userInfo map { it =>
				it indexOf (':') match {
					case -1	=> Cred(it, "")
					case x	=> Cred(it.substring(0,x), it.substring(x+1))
				}
			}
	def defaultPort:Option[Int] = 
			scheme map { _ match { 
				case "http"		=> 80	
				case "https"	=> 443
				case x			=> error("unexpected scheme: " + x)
			} }
}