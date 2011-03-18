package scmw.web

object NoProxy {
	def all(target:Target):Boolean	= false
	
	/** like suns format, f.e. "*.test.de|localhost" */
	def sun(re:String)(target:Target):Boolean	=  target.host matches re.replaceAll("\\.", "\\\\.").replaceAll("\\*", ".*?")
}
