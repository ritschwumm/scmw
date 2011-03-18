import sbt._
      
final class ScMwProject(info:ProjectInfo) extends DefaultProject(info) {
	// dependencies
	val scutil	= "de.djini"	%% "scutil"	% "0.0.1"	% "compile" withSources;
	val scjson	= "de.djini"	%% "scjson"	% "0.0.1"	% "compile" withSources;
	
	// issue compiler warnings
	override def compileOptions	= super.compileOptions ++ Seq(Unchecked)
		
	// publish sources
	override def packageSrcJar	= defaultJarPath("-sources.jar")
	val sourceArtifact	= Artifact.sources(artifactID)
	override def packageToPublishActions = super.packageToPublishActions ++ Seq(packageSrc)
	
	/*
	override def packageDocsJar	= defaultJarPath("-javadoc.jar")
	override def packageSrcJar	= defaultJarPath("-sources.jar")
	val sourceArtifact	= Artifact.sources(artifactID)
	val docsArtifact	= Artifact.javadoc(artifactID)
	override def packageToPublishActions = super.packageToPublishActions ++ Seq(packageDocs, packageSrc)
	*/
}
