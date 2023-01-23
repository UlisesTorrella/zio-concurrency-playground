import zio._
import zio.Console._

case class App(name: String) {
  val app: ZIO[Any, Any, Any] = for {
    _ <- printLine("Undefined program")
  } yield ()
}