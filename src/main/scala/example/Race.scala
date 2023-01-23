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

object RaceApp extends App("race"){

  val n = 460

  override val app = for {
      _      <- printLine(s"Running a recursive factorial vs loop factorial using n=${n}")
      fRun   <- wrapRecFact.race(wrapLoopFact)
      sRun   <- wrapLoopFact.race(wrapRecFact)
      _      <- printLine(s"The winner for the first run is: ${fRun}")
      _      <- printLine(s"The winner for the second run is: ${sRun}")
  } yield ()


  private def wrapRecFact = 
      ZIO.attempt(fact(45)) *> ZIO.succeed("rectFact")
      
  private def wrapLoopFact = 
      ZIO.attempt(loopFact(45)) *> ZIO.succeed("loopFact")

}