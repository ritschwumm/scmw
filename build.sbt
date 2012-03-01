name			:= "scmw"

organization	:= "de.djini"

version			:= "0.0.4"

scalaVersion	:= "2.9.1"

//publishArtifact in (Compile, packageBin)	:= false

publishArtifact in (Compile, packageDoc)	:= false

publishArtifact in (Compile, packageSrc)	:= false

libraryDependencies	++= Seq(
	"de.djini"					%% "scutil"		% "0.0.5"	% "compile",
	"de.djini"					%% "scjson"		% "0.0.5"	% "compile",
	"org.apache.httpcomponents"	% "httpclient"	% "4.1.2"	% "compile",
	"org.apache.httpcomponents"	% "httpmime"	% "4.1.2"	% "compile"
)

scalacOptions	++= Seq("-deprecation", "-unchecked")
