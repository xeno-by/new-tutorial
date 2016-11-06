scalaVersion in ThisBuild := "2.11.8"

// New-style macro annotations are under active development.
// As a result, in this build we'll be referring to snapshot versions
// of both scala.meta and macro paradise.
lazy val snapshotResolver = (
  resolvers += Resolver.url(
    "scalameta",
    url("http://dl.bintray.com/scalameta/maven"))(Resolver.ivyStylePatterns)
)

// A dependency on scala.meta is required to write new-style macros,
// but not to expand such macros.
// This is similar to how it works for old-style macros and
// a dependency on scala.reflect.
lazy val metaDependency = Seq(
  snapshotResolver,
  libraryDependencies += "org.scalameta" %% "scalameta" % "1.3.0.522"
)

// A dependency on macro paradise 3.x is required
// to both write and expand new-style macros.
// This is similar to how it works for old-style macro annotations and
// a dependency on macro paradise 2.x.
lazy val paradiseDependency = Seq(
  snapshotResolver,
  addCompilerPlugin("org.scalameta" % "paradise" % "3.0.0.109" cross CrossVersion.full),
  scalacOptions += "-Xplugin-require:macroparadise"
)

lazy val macros = project.settings(
  metaDependency,
  paradiseDependency
)

lazy val app = project.settings(
  paradiseDependency,
  scalacOptions in (Compile, console) := Nil // workaround for #10
).dependsOn(macros)
