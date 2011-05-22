package scmw.web

import scala.collection.JavaConversions._
import scala.collection.mutable

import scutil.ext.AnyRefImplicits._

object Proxy {
	def systemProperties:Option[Proxy] = {
		val	sysProps:mutable.Map[String,String]	= System.getProperties
		for {
			host	<- sysProps get "http.proxyHost"
			port	<- sysProps get "http.proxyPort"
		}
		yield {
			val	target	= Target(host, Integer.parseInt(port))
			val noproxy	= sysProps get "http.nonProxyHosts" map NoProxy.sun _
			Proxy(target, None, noproxy)
		}
	}
	
	def environmentVariable:Option[Proxy] =
			for {
				http_proxy	<- System getenv "http_proxy" guardNotNull;
				data		= URIData parse http_proxy
				target		<- data.target
			} 
			yield {
				Proxy(target, data.cred, None)
			}
}

case class Proxy(target:Target, cred:Option[Cred], noproxy:Option[Target=>Boolean])
