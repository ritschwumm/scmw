package scmw

import java.io._

import org.apache.http.entity.mime.content._
import org.apache.http.entity.mime.MIME

import scutil.Implicits._

final class ProgressFileBody(file:File, mimeType:String, progress:Long=>Unit) extends AbstractContentBody(mimeType) {
	require(file != null, "File may not be null")
 
	def getInputStream():InputStream	= new ProgressInputStream(new FileInputStream(file), progress)
   
	override def writeTo(out:OutputStream) {
		require(out != null, "Output stream may not be null")
		getInputStream() use { _ copyTo out }
		out.flush()
	}
	
	def getTransferEncoding():String	= MIME.ENC_BINARY
	def getCharset():String				= null
	def getContentLength():Long			= file.length
	def getFilename():String			= file.getName
	def getFile():File					= file
}
