name			:= "scmw"
organization	:= "de.djini"
version			:= "0.81.0"

scalaVersion	:= "2.11.7"
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
	"de.djini"					%%	"scutil-core"	% "0.80.0"	% "compile",
	"de.djini"					%%	"scjson"		% "0.85.0"	% "compile",
	"org.apache.httpcomponents"	%	"httpclient"	% "4.5.1"	% "compile",
	"org.apache.httpcomponents"	%	"httpmime"		% "4.5.1"	% "compile"
)

enablePlugins(BuildInfoPlugin)

//------------------------------------------------------------------------------

buildInfoKeys		:= Seq[BuildInfoKey](name, version)	// name, version, scalaVersion, sbtVersion
buildInfoPackage	:= "scmw"
