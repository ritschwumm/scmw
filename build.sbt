name			:= "scmw"

organization	:= "de.djini"

version			:= "0.19.0"

scalaVersion	:= "2.10.2"

libraryDependencies	++= Seq(
	"de.djini"					%%	"scutil"		% "0.22.0"	% "compile",
	"de.djini"					%%	"scjson"		% "0.24.0"	% "compile",
	"org.apache.httpcomponents"	%	"httpclient"	% "4.2.5"	% "compile",
	"org.apache.httpcomponents"	%	"httpmime"		% "4.2.5"	% "compile"
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
