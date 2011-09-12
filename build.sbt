name			:= "scmw"

organization	:= "de.djini"

version			:= "0.0.3"

scalaVersion	:= "2.9.0-1"

//publishArtifact in (Compile, packageBin)	:= false

publishArtifact in (Compile, packageDoc)	:= false

publishArtifact in (Compile, packageSrc)	:= false

libraryDependencies	++= Seq(
	"de.djini"					%% "scutil"		% "0.0.4"	% "compile",
	"de.djini"					%% "scjson"		% "0.0.4"	% "compile",
	"org.apache.httpcomponents"	% "httpclient"	% "4.1.1"	% "compile",
	"org.apache.httpcomponents"	% "httpmime"	% "4.1.1"	% "compile"
)

scalacOptions	++= Seq("-deprecation", "-unchecked")
