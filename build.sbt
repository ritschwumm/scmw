name			:= "scmw"

organization	:= "de.djini"

version			:= "0.8.0"

scalaVersion	:= "2.9.2"

libraryDependencies	++= Seq(
	"de.djini"					%%	"scutil"		% "0.13.0"	% "compile",
	"de.djini"					%%	"scmirror"		% "0.9.0"	% "compile",
	"de.djini"					%%	"scjson"		% "0.13.0"	% "compile",
	"org.apache.httpcomponents"	%	"httpclient"	% "4.2.2"	% "compile",
	"org.apache.httpcomponents"	%	"httpmime"		% "4.2.2"	% "compile"
)

scalacOptions	++= Seq("-deprecation", "-unchecked")
