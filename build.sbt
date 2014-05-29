name			:= "scmw"

organization	:= "de.djini"

version			:= "0.44.0"

scalaVersion	:= "2.11.0"

libraryDependencies	++= Seq(
	"de.djini"					%%	"scutil-core"	% "0.44.0"	% "compile",
	"de.djini"					%%	"scjson"		% "0.49.0"	% "compile",
	"org.apache.httpcomponents"	%	"httpclient"	% "4.3.2"	% "compile",
	"org.apache.httpcomponents"	%	"httpmime"		% "4.3.2"	% "compile"
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
