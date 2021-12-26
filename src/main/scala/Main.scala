import cats.effect.*
import cats.effect.unsafe.implicits.global
import cats.implicits.*
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.dsl.Http4sDsl
import org.http4s.dsl.io.*
import org.http4s.implicits.*
import org.http4s.server.Router
import org.http4s.syntax.*
import org.http4s.{EntityEncoder, HttpApp, HttpRoutes}

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] =
    val helloWorldService = SimpleHttpRoutes.helloWorldRoutes[IO](HelloWorld.impl[IO])
    val tweetService =  SimpleHttpRoutes.tweetRoutes[IO](Tweet.impl[IO])
    val services = tweetService <+> helloWorldService
    val httpApp = Router("/" -> helloWorldService, "/api" -> services).orNotFound

    BlazeServerBuilder[IO]
      .bindHttp(9999, "localhost")
      .withHttpApp(httpApp)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
}
