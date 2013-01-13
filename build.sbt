name			:= "scmw"

organization	:= "de.djini"

version			:= "0.6.0"

scalaVersion	:= "2.9.2"

libraryDependencies	++= Seq(
	"de.djini"					%%	"scutil"		% "0.10.0"	% "compile",
	"de.djini"					%%	"scmirror"		% "0.6.0"	% "compile",
	"de.djini"					%%	"scjson"		% "0.10.0"	% "compile",
	"org.apache.httpcomponents"	%	"httpclient"	% "4.2.1"	% "compile",
	"org.apache.httpcomponents"	%	"httpmime"		% "4.2.1"	% "compile"
)

scalacOptions	++= Seq("-deprecation", "-unchecked")
