import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "comicserver"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    anorm,
    "com.top10" %% "scala-redis-client" % "1.13.0",
    "org.scalaj" % "scalaj-http_2.10" % "0.3.9" exclude("junit", "junit"),
    "com.github.nscala-time" %% "nscala-time" % "0.4.2"
  )
  


  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
//    resolvers += "Sonatype" at "https:/u/oss.sonatype.org/service/local/staging/deploy/maven2/"
    resolvers += "Sonatype OSS Releases" at "http://oss.sonatype.org/content/repositories/releases/"
  )

}
