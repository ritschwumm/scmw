name			:= "scmw"

organization	:= "de.djini"

version			:= "0.29.0"

scalaVersion	:= "2.10.3"

libraryDependencies	++= Seq(
	"de.djini"					%%	"scutil"		% "0.31.0"	% "compile",
	"de.djini"					%%	"scjson"		% "0.34.0"	% "compile",
	"org.apache.httpcomponents"	%	"httpclient"	% "4.2.6"	% "compile",
	"org.apache.httpcomponents"	%	"httpmime"		% "4.2.6"	% "compile"
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
