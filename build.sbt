name := "qlib"

version := "1.3.2"

scalaVersion in ThisBuild := "2.12.15"

classpathTypes ++= Set("jnilib")

resolvers += MavenRepository("jogamp", "http://jogamp.org/deployment/maven")
resolvers += MavenRepository("jitpack.io", "https://jitpack.io")

libraryDependencies += "com.github.micycle1" % "processing-core-4" % "4.0.3"

libraryDependencies += "org.typelevel" %% "spire" % "0.14.1"

libraryDependencies += "org.jogamp.jogl" % "jogl-all" % "2.3.2"

libraryDependencies += "org.jogamp.jogl" % "newt" % "2.3.2"

libraryDependencies += "org.jogamp.gluegen" % "gluegen-rt" % "2.3.2"


fork in run := true
