import zio._
import zio.Console._
import java.util.concurrent.TimeUnit

def fact(n: Int): Long = n match
  case 0 => 1
  case _ => n * fact(n-1)

def loopFact(n: Int): Long = {
  var f = 1
  for(i <- 1 to n)
  {
    f= f*i
  }
  f
}

def wrapRecFact = 
    ZIO.attempt(fact(45)) *> ZIO.succeed("rectFact")
    
def wrapLoopFact = 
    ZIO.attempt(loopFact(45)) *> ZIO.succeed("loopFact")

def wait2seconds =
  ZIO.sleep(2.seconds) *> ZIO.succeed("2 seconds waited")

def wait3seconds =
  ZIO.sleep(3.seconds) *> ZIO.succeed("3 seconds waited")

object MyApp extends ZIOAppDefault {

  def run = raceApp // sequentialApp

  val sequentialApp =
    for {
      _      <- Clock.currentDateTime.flatMap(Console.printLine(_))
      start  <- Clock.currentTime(TimeUnit.MILLISECONDS)
      fiber1 <- wait2seconds.fork
      fiber2 <- wait3seconds.fork
      fiber  = fiber1.zip(fiber2)
      msg    <- fiber.join
      end    <- Clock.currentTime(TimeUnit.MILLISECONDS)
      _      <- printLine(s"It took me: ${(end-start).toString} millisecond to run this " +
        s"program and the result is ${msg}")
    } yield ()


  val n = 460
  val raceApp =
    for {
      _      <- printLine(s"Running a recursive factorial vs loop factorial using n=${n}")
      start  <- Clock.currentTime(TimeUnit.MILLISECONDS)
      fRun   <- wrapRecFact.race(wrapLoopFact)
      sRun   <- wrapLoopFact.race(wrapRecFact)
      end    <- Clock.currentTime(TimeUnit.MILLISECONDS)
      _      <- printLine(s"The winner for the first run is: ${fRun}")
      _      <- printLine(s"The winner for the second run is: ${sRun}")
    } yield ()
}