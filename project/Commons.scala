import sbt.Keys._
import sbt._
import sbtassembly.MergeStrategy
import sbtprotoc.ProtocPlugin.autoImport.PB

object Commons {

//  import Environment.{buildEnv, BuildEnv}
  import sbtassembly.AssemblyKeys.assembly
  import sbtassembly.AssemblyKeys.assemblyMergeStrategy
  import sbtassembly.MergeStrategy
  import sbtassembly.PathList

  def basicSettings =
    Seq(
      organization := "helloscala",
      organizationName := "helloscala fusion",
      organizationHomepage := Some(url("https://akka-fusion.helloscala.com/")),
      homepage := Some(url("https://helloscala.com/")),
      startYear := Some(2019),
      scalacOptions ++= {
        var list = Seq(
          "-encoding",
          "UTF-8", // yes, this is 2 args
          "-feature",
          "-deprecation",
          "-unchecked",
          "-Xlint",
          "-Ywarn-dead-code")
        if (scalaVersion.value.startsWith("2.12")) {
          list ++= Seq("-opt:l:inline", "-opt-inline-from")
        }
//        if (buildEnv.value != BuildEnv.Developement) {
//          list ++= Seq("-Xelide-below", "2001")
//        }
        list
      },
      javacOptions in Compile ++= Seq("-Xlint:unchecked", "-Xlint:deprecation"),
      javaOptions in run ++= Seq("-Xms128m", "-Xmx1024m", "-Djava.library.path=./target/native"),
      shellPrompt := { s =>
        Project.extract(s).currentProject.id + " > "
      },
      test in assembly := {},
      assemblyMergeStrategy in assembly := {
        case PathList("javax", "servlet", xs @ _*)                => MergeStrategy.first
        case PathList("io", "netty", xs @ _*)                     => MergeStrategy.first
        case PathList("jnr", xs @ _*)                             => MergeStrategy.first
        case PathList("com", "datastax", xs @ _*)                 => MergeStrategy.first
        case PathList("com", "kenai", xs @ _*)                    => MergeStrategy.first
        case PathList("org", "objectweb", xs @ _*)                => MergeStrategy.first
        case PathList("com", "google", "protobuf", xs @ _*)       => MergeStrategy.first
        case PathList("scalapb", "options", xs @ _*)              => MergeStrategy.first
        case PathList(ps @ _*) if ps.last.endsWith(".html")       => MergeStrategy.first
        case "application.conf"                                   => MergeStrategy.concat
        case "module-info.class"                                  => MergeStrategy.concat
        case "META-INF/io.netty.versions.properties"              => MergeStrategy.first
        case PathList("org", "slf4j", xs @ _*)                    => MergeStrategy.first
        case "META-INF/native/libnetty-transport-native-epoll.so" => MergeStrategy.first
        case x =>
          val oldStrategy = (assemblyMergeStrategy in assembly).value
          oldStrategy(x)
      },
      fork in run := true,
      fork in Test := true,
      parallelExecution in Test := false) // ++ Environment.settings
}

object Publishing {

  lazy val publishing = Seq(
    publishTo := (if (version.value.endsWith("-SNAPSHOT")) {
                    Some("Helloscala_sbt-public_snapshot".at(
                      "https://artifactory.hongkazhijia.com/artifactory/sbt-release;build.timestamp=" + new java.util.Date().getTime))
                  } else {
                    Some(
                      "Helloscala_sbt-public_release".at(
                        "https://artifactory.hongkazhijia.com/artifactory/libs-release"))
                  }),
    credentials += Credentials(Path.userHome / ".ivy2" / ".credentials_ihongka"))

  lazy val noPublish =
    Seq(publish := ((): Unit), publishLocal := ((): Unit), publishTo := None)
}

object Environment {

  object BuildEnv extends Enumeration {
    val Production, Stage, Test, Developement = Value
  }

  lazy val buildEnv = settingKey[BuildEnv.Value]("The current build environment")

  lazy val settings = Seq(onLoadMessage := {
    // old message as well
    val defaultMessage = onLoadMessage.value
    val env = buildEnv.value
    s"""|$defaultMessage
          |Working in build environment: $env""".stripMargin
  })

}

object Packaging {
  // Good example https://github.com/typesafehub/activator/blob/master/project/Packaging.scala
  import Environment.buildEnv
  import Environment.BuildEnv
  import com.typesafe.sbt.SbtNativePackager._
  import com.typesafe.sbt.packager.Keys._

  // This is dirty, but play has stolen our keys, and we must mimc them here.
  val stage = TaskKey[File]("stage")
  val dist = TaskKey[File]("dist")

  val settings = Seq(
    name in Universal := s"${name.value}",
    dist := (packageBin in Universal).value,
    mappings in Universal += {
      val confFile = buildEnv.value match {
        case BuildEnv.Developement => "dev.conf"
        case BuildEnv.Test         => "test.conf"
        case BuildEnv.Stage        => "stage.conf"
        case BuildEnv.Production   => "prod.conf"
      }
      (sourceDirectory(_ / "universal" / "conf").value / confFile) -> "conf/application.conf"
    },
    bashScriptExtraDefines ++= Seq(
        """addJava "-Dconfig.file=${app_home}/../conf/application.conf"""",
        """addJava "-Dpidfile.path=${app_home}/../run/%s.pid"""".format(name.value),
        """addJava "-Dlogback.configurationFile=${app_home}/../conf/logback.xml""""),
    bashScriptConfigLocation := Some("${app_home}/../conf/jvmopts"),
    scriptClasspath := Seq("*"),
    mappings in (Compile, packageDoc) := Seq())

  // Create a new MergeStrategy for aop.xml files
  val aopMerge: MergeStrategy = new MergeStrategy {
    val name = "aopMerge"
    import scala.xml._
    import scala.xml.dtd._

    def apply(tempDir: File, path: String, files: Seq[File]): Either[String, Seq[(File, String)]] = {
      val dt =
        DocType("aspectj", PublicID("-//AspectJ//DTD//EN", "http://www.eclipse.org/aspectj/dtd/aspectj.dtd"), Nil)
      val file = MergeStrategy.createMergeTarget(tempDir, path)
      val xmls: Seq[Elem] = files.map(XML.loadFile)
      val aspectsChildren: Seq[Node] = xmls.flatMap(_ \\ "aspectj" \ "aspects" \ "_")
      val weaverChildren: Seq[Node] = xmls.flatMap(_ \\ "aspectj" \ "weaver" \ "_")
      val options: String = xmls.map(x => (x \\ "aspectj" \ "weaver" \ "@options").text).mkString(" ").trim
      val weaverAttr = if (options.isEmpty) Null else new UnprefixedAttribute("options", options, Null)
      val aspects = new Elem(null, "aspects", Null, TopScope, false, aspectsChildren: _*)
      val weaver = new Elem(null, "weaver", weaverAttr, TopScope, false, weaverChildren: _*)
      val aspectj = new Elem(null, "aspectj", Null, TopScope, false, aspects, weaver)
      XML.save(file.toString, aspectj, "UTF-8", xmlDecl = false, dt)
      IO.append(file, IO.Newline.getBytes(IO.defaultCharset))
      Right(Seq(file -> path))
    }
  }

}

object Protobufs {
  def protocVersion = PB.protocVersion := "-v371"

  def protobufSettings =
    Seq(
      protocVersion,
      PB.targets in Compile := Seq(scalapb.gen(flatPackage = true) -> (sourceManaged in Compile).value),
      libraryDependencies ++= Seq(
          "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf"))
}
