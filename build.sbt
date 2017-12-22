name			:= "scmw"
organization	:= "de.djini"
version			:= "0.138.0"

scalaVersion	:= "2.12.4"
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
	"-Xfatal-warnings",
	"-Xlint"
)

conflictManager	:= ConflictManager.strict
libraryDependencies	++= Seq(
	"de.djini"					%%	"scutil-core"	% "0.130.0"	% "compile",
	"de.djini"					%%	"scjson-codec"	% "0.143.0"	% "compile",
	"org.apache.httpcomponents"	%	"httpclient"	% "4.5.4"	% "compile",
	"org.apache.httpcomponents"	%	"httpmime"		% "4.5.4"	% "compile"
)

wartremoverErrors ++= Seq(
	Wart.StringPlusAny,
	Wart.EitherProjectionPartial,
	Wart.OptionPartial,
	Wart.Enumeration,
	Wart.FinalCaseClass,
	Wart.JavaConversions,
	Wart.Option2Iterable,
	Wart.TryPartial
)

enablePlugins(BuildInfoPlugin)

//------------------------------------------------------------------------------

buildInfoKeys		:= Seq[BuildInfoKey](name, version)	// name, version, scalaVersion, sbtVersion
buildInfoPackage	:= "scmw"
