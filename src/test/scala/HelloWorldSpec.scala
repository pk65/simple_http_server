import cats.effect.IO
import munit.CatsEffectSuite
import org.http4s.*
import org.http4s.Status.NotFound
import org.http4s.implicits.*
import org.http4s.server.Router

class HelloWorldSpec extends CatsEffectSuite {
  private def get(uri: Uri) =
    val request = Router("/" -> SimpleHttpRoutes.helloWorldRoutes[IO])
      .run(Request(method = Method.GET, uri = uri))
    request.getOrElse(Response[IO](NotFound))

  test("HelloWorld returns hello world message") {
    val retHelloWorld = get(uri"/hello/other-person")
    for {
      _ <- assertIO(retHelloWorld.flatMap(_.as[String]), "{\"message\":\"Hello, other-person\"}")
      _ <- assertIO(retHelloWorld.map(_.status), Status.Ok)
    } yield ()
  }
}
