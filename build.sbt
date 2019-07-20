name := "titanic-sparkml"

version := "0.1"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.2.0",
  "org.apache.spark" %% "spark-sql" % "2.2.0",
  "org.apache.spark" %% "spark-streaming" % "2.2.0",
  "org.apache.spark" %% "spark-mllib" % "2.2.0",
  "com.github.hirofumi" %% "xgboost4j-spark" % "0.7.1-p1"
)

assemblyMergeStrategy in assembly := {
  case m if m.toLowerCase.endsWith("manifest.mf")               => MergeStrategy.discard
  case m if m.toLowerCase.matches("meta-inf.*\\.sf$")   => MergeStrategy.discard
  case "log4j.properties"                                       => MergeStrategy.discard
  case m if m.toLowerCase.startsWith("meta-inf/services/")      => MergeStrategy.filterDistinctLines
  case "reference.conf"                                         => MergeStrategy.concat
  case _                                                        => MergeStrategy.first
}