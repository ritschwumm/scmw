name			:= "scmw"
organization	:= "de.djini"
version			:= "0.67.0"

scalaVersion	:= "2.11.6"
scalacOptions	++= Seq(
	"-deprecation",
	"-unchecked",
	"-language:implicitConversions",
	// "-language:existentials",
	// "-language:higherKinds",
	"-language:reflectiveCalls",
	// "-language:dynamics",
	// "-language:postfixOps",
	// "-language:experimental.macros"
	"-feature",
	"-Ywarn-unused-import",
	"-Xfatal-warnings"
)

conflictManager	:= ConflictManager.strict
libraryDependencies	++= Seq(
	"de.djini"					%%	"scutil-core"	% "0.66.0"	% "compile",
	"de.djini"					%%	"scjson"		% "0.71.0"	% "compile",
	"org.apache.httpcomponents"	%	"httpclient"	% "4.4"		% "compile",
	"org.apache.httpcomponents"	%	"httpmime"		% "4.4"		% "compile"
)

//------------------------------------------------------------------------------

buildInfoSettings
sourceGenerators in Compile	<+= buildInfo
buildInfoKeys		:= Seq[BuildInfoKey](name, version)	// name, version, scalaVersion, sbtVersion
buildInfoPackage	:= "scmw"
