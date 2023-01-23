import zio._
import zio.Console._
import java.util.concurrent.TimeUnit

def wait2seconds =
  ZIO.sleep(2.seconds) *> ZIO.succeed("2 seconds waited")

def wait3seconds =
  ZIO.sleep(3.seconds) *> ZIO.succeed("3 seconds waited")

object WaitApp extends App("wait") {
  override val app =
    for {
      _      <- Clock.currentDateTime.flatMap(Console.printLine(_))
      fiber1 <- wait2seconds.fork
      fiber2 <- wait3seconds.fork
      fiber  = fiber1.zip(fiber2)
      msg    <- fiber.join
    } yield msg

}