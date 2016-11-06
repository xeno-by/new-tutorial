## Use scala.meta to write macros

### New-style macro annotations

Scala.meta makes it possible to write new-style macro annotations.
In comparison with [the state of the art based on scala.reflect](http://docs.scala-lang.org/overviews/macros/annotations.html),
new-style macro annotations are:
  * *Lightweight*. The new macro system no longer features the split between macro defs and macro impls.
    Moreover, writing macros no longer requires carrying around a context and juggling path-dependent types.
    As a result, macros can be defined with much less ceremony.
  * *Portable*. New-style macros are based on a platform-independent metaprogramming API defined in scala.meta.
    Unlike scala.reflect, scala.meta doesn't depend on compiler internals, so macros based on scala.meta
    can be run in a multitude of environments, including Scala, IntelliJ IDEA and Dotty.

In order to define a new-style macro annotation, create a class that extends `StaticAnnotation`
and create an `inline` apply method with a `meta` block in it. Inside the `meta` block, you can take apart
the annotated member and generate new code using [scala.meta quasiquotes](https://github.com/scalameta/scalameta/blob/master/notes/quasiquotes.md).
`inline` and `meta` are new language constructs introduced by macro paradise 3.x.

```scala
import scala.meta._

class main extends scala.annotation.StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    val q"object $name { ..$stats }" = defn
    val main = q"def main(args: Array[String]): Unit = { ..$stats }"
    q"object $name { $main }"
  }
}
```

Using new-style macro annotations isn't different from using traditional macro annotations.
You put an annotation on a definition and then this definition gets replaced with the result of a macro expansion.

```scala
@main
object MyApp {
  println("Hello Scala.meta macros!")
}
```

### New-style def macros

Scala.meta doesn't yet provide a possibility to write new-style def macros, but we are working hard on implementing this functionality.
Attend [Eugene Burmako's talk at Scala eXchange 2016](https://skillsmatter.com/conferences/7432-scala-exchange-2016#program) to learn more about our progress.

### Compatibility with traditional macros

At the moment, new-style macros can only take apart existing Scala syntax and generate new syntax (so called *syntactic API*).
This corresponds to the functionality provided by traditional macro annotations
that only use tree constructors and quasiquotes.

Even this limited functionality should be enough to port most of the existing macro annotations
to scala.meta. Oleksandr Olgashko [has ported a large subset of Simulacrum's @typeclass features to new-style macros](https://gitter.im/mpilquist/simulacrum?at=57fd4a7e68f560d80cf89330),
so we are confident that new-style macros are powerful enough to support even more complex annotations.

For new-style def macros, we are working on *semantic API*, which will provide compiler information such as type inference,
name resolution and other functionality that requires typechecking. It is too early to tell how compatible this API
will be with what is provided by scala.reflect. We will provide more information as the design of the semantic API shapes up.

### Environment setup

## SBT

[build.sbt](https://github.com/xeno-by/tutorial/blob/macros/build.sbt) explains how to set up SBT projects
that define and use new-style macros. In comparison with traditional macros, there are two differences:
  * A dependency on scala.reflect is replaced with a dependency on scala.meta
  * A dependency on macro paradise 3.x is required to both define and expand new-style macros

## IntelliJ IDEA

Thanks to the efforts of Mikhail Mutcianko, new-style macros enjoy unprecedented support in IntelliJ IDEA.

With the latest build of the Scala plugin, it is possible to interactively expand new-style macro annotations,
obtain information about expansion errors and see generated members in completions.
Check out [the IntelliJ IDEA Scala plugin blog](TODO) to learn more
about supported functionality.

In order to open the accompanying project in IntelliJ IDEA:
  * TODO
  * TODO
  * TODO

## Eclipse

TODO

