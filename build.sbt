name			:= "scmw"

organization	:= "de.djini"

version			:= "0.13.0"

scalaVersion	:= "2.10.0"

libraryDependencies	++= Seq(
	"de.djini"					%%	"scutil"		% "0.17.0"	% "compile",
	"de.djini"					%%	"scjson"		% "0.18.0"	% "compile",
	"org.apache.httpcomponents"	%	"httpclient"	% "4.2.3"	% "compile",
	"org.apache.httpcomponents"	%	"httpmime"		% "4.2.3"	% "compile"
)

scalacOptions	++= Seq(
	"-deprecation",
	"-unchecked",
	"-language:implicitConversions",
	// "-language:existentials",
	// "-language:higherKinds",
	"-language:reflectiveCalls",
	// "-language:dynamics",
	"-language:postfixOps",
	// "-language:experimental.macros"
	"-feature"
)
