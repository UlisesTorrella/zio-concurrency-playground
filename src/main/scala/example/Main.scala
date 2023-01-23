import zio._
import zio.Console._
import java.util.concurrent.TimeUnit


object MyApp extends ZIOAppDefault {

  def run = for {
      args <- getArgs
      app: App  =
        if (args.isEmpty)
          App("none")
        else
          args.head match 
            case "wait" => WaitApp
            case "race" => RaceApp
            case _      => App("none")
      start  <- Clock.currentTime(TimeUnit.MILLISECONDS)
      _      <- app.app
      end    <- Clock.currentTime(TimeUnit.MILLISECONDS)
      _      <- printLine(s"This program was executed in: ${(end-start).toString} milliseconds")
    } yield ()
}