name			:= "scmw"

organization	:= "de.djini"

version			:= "0.55.0"

scalaVersion	:= "2.11.2"

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

conflictManager	:= ConflictManager.strict

libraryDependencies	++= Seq(
	"de.djini"					%%	"scutil-core"	% "0.54.0"	% "compile",
	"de.djini"					%%	"scjson"		% "0.59.0"	% "compile",
	"org.apache.httpcomponents"	%	"httpclient"	% "4.3.5"	% "compile",
	"org.apache.httpcomponents"	%	"httpmime"		% "4.3.5"	% "compile"
)

//------------------------------------------------------------------------------

buildInfoSettings

sourceGenerators in Compile	<+= buildInfo

buildInfoKeys		:= Seq[BuildInfoKey](name, version)	// name, version, scalaVersion, sbtVersion

buildInfoPackage	:= "scmw"
