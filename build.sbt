name			:= "scmw"

organization	:= "de.djini"

version			:= "0.38.0"

scalaVersion	:= "2.10.3"

libraryDependencies	++= Seq(
	"de.djini"					%%	"scutil"		% "0.38.0"	% "compile",
	"de.djini"					%%	"scjson"		% "0.43.0"	% "compile",
	"org.apache.httpcomponents"	%	"httpclient"	% "4.3.1"	% "compile",
	"org.apache.httpcomponents"	%	"httpmime"		% "4.3.1"	% "compile"
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

//------------------------------------------------------------------------------

buildInfoSettings

sourceGenerators in Compile	<+= buildInfo

buildInfoKeys		:= Seq[BuildInfoKey](name, version)	// name, version, scalaVersion, sbtVersion

buildInfoPackage	:= "scmw"
