package scmw

import java.util.{ArrayList=>JList}
import java.io.File
import java.io.InputStream
import java.io.FileInputStream
import java.net.ProxySelector
import java.nio.charset.Charset

import org.apache.http._
import org.apache.http.auth._
import org.apache.http.message._
import org.apache.http.entity.mime._
import org.apache.http.entity.mime.content._
import org.apache.http.client.methods._
import org.apache.http.client.entity._
import org.apache.http.client.params._ 
import org.apache.http.client.utils._ 
import org.apache.http.impl.client._
import org.apache.http.impl.conn._ 
import org.apache.http.impl.conn.PoolingClientConnectionManager
import org.apache.http.util.EntityUtils

import scutil.Implicits._
import scutil.io.Charsets._
import scutil.log._

import scjson._
import scjson.codec._

import scmw.web._

final class Connection(apiURL:String) extends Logging {
	private val apiTarget	= (URIData parse apiURL).target getOrError ("invalid api url: " + apiURL)
	private val charSet		= utf_8
	private val userAgent	= "scmw/0.0"
	
	private val manager	= new PoolingClientConnectionManager
	manager setDefaultMaxPerRoute	6
	manager setMaxTotal				18
	 
	private val client	= new DefaultHttpClient(manager)
	client.getParams setParameter (ClientPNames.COOKIE_POLICY,		CookiePolicy.BROWSER_COMPATIBILITY)
	client.getParams setParameter (ClientPNames.HANDLE_REDIRECTS,	false)
	client.getParams setParameter (ClientPNames.DEFAULT_HEADERS,	arrayList(Seq(new BasicHeader("User-Agent", userAgent)))) 
	client	setRoutePlanner	new ProxySelectorRoutePlanner(
			client.getConnectionManager.getSchemeRegistry,
			ProxySelector.getDefault)
	
	def dispose() {
		manager.shutdown()
	}
	
	//------------------------------------------------------------------------------
	
	def GET(params:Seq[(String,String)]):Option[JSONValue] = {
		val	queryString	= URLEncodedUtils format (nameValueList(params), charSet.name)
		val	request		= new HttpGet(apiURL + "?" + queryString)
		handle(request)
	}
	
	def POST(params:Seq[(String,String)]):Option[JSONValue] = {
		val	requestEntity	= new UrlEncodedFormEntity(nameValueList(params), charSet.name)
		val request			= new HttpPost(apiURL)
		request	setEntity	requestEntity
		handle(request)
	}
	
	def POST_multipart(params:Seq[(String,String)], fileField:String, file:File, progress:Long=>Unit):Option[JSONValue] = {
		val requestEntity	= new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, null)
		params foreach { 
			// NOTE was Content-Transfer-Encoding: 8bit
			case (key, value)	=> requestEntity addPart (key, new StringBody(value, "text/plain", charSet))
		}
		// NOTE was Content-Type: application/octet-stream; charset=UTF-8
		// NOTE was Content-Transfer-Encoding: binary
		requestEntity addPart (fileField, new ProgressFileBody(file, "application/octet-stream", progress))
		val request			= new HttpPost(apiURL)
		request	setEntity	requestEntity
		handle(request)
	}
	
	private def handle(request:HttpUriRequest):Option[JSONValue] = {
		DEBUG(request.getRequestLine)
		val	response		= client execute request
		DEBUG(response.getStatusLine)
		require(
				response.getStatusLine.getStatusCode == 200,	
				"unexpected response: " + response.getStatusLine)
		/*
		val string	= response.getEntity.guardNotNull map EntityUtils.toString
		val start	= string map { it =>
			val limit	= 500
			if (it.length < limit)	it
			else					it.substring(0, limit)
		}
		DEBUG(start)
		string flatMap JSParser.parse
		*/
		response.getEntity.guardNotNull map EntityUtils.toString flatMap { JSONCodec decode _ toOption }
	}
	
	private def nameValueList(kv:Seq[(String,String)]):JList[NameValuePair]	=
			arrayList(kv map nameValue)
			
	private def nameValue(kv:(String,String)):NameValuePair	=
			new BasicNameValuePair(kv._1, kv._2)
	
	private def arrayList[T](elements:Seq[T]):JList[T]	=
			new JList[T] |>> { al => elements foreach al.add }
	
	//------------------------------------------------------------------------------
			
	/** 
	 * set credentials for a host
	 * user and password may be null to disable 
	 */
	 def identify(cred:Option[Cred]) {
		// TODO we don't want to be asked
		// client.getParams	setAuthenticationPreemptive true
		
		client.getCredentialsProvider setCredentials (
			new AuthScope(
					apiTarget.host,
					apiTarget.port,
					AuthScope.ANY_REALM,
					AuthScope.ANY_SCHEME),
			cred map clientCred orNull)
	}
	
	private def clientCred(cred:Cred):UsernamePasswordCredentials = 
			new UsernamePasswordCredentials(cred.user, cred.password)
}
