name			:= "scmw"

organization	:= "de.djini"

version			:= "0.46.0"

scalaVersion	:= "2.11.1"

libraryDependencies	++= Seq(
	"de.djini"					%%	"scutil-core"	% "0.45.0"	% "compile",
	"de.djini"					%%	"scjson"		% "0.50.0"	% "compile",
	"org.apache.httpcomponents"	%	"httpclient"	% "4.3.4"	% "compile",
	"org.apache.httpcomponents"	%	"httpmime"		% "4.3.4"	% "compile"
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
