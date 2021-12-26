import cats.effect.unsafe.implicits.global
import cats.effect.{IO, LiftIO}
import cats.implicits.*
import io.circe.*
import io.circe.syntax.*
import org.http4s.circe.*
import org.http4s.dsl.io.*
import org.http4s.implicits.*
import org.http4s.server.Router
import org.http4s.*
import org.junit.Assert.*
import org.junit.{Before, Test}

class Test1:
  def check[A](actual:         IO[Response[IO]],
               expectedStatus: Status,
               expectedBody:   Option[A])(
                implicit ev: EntityDecoder[IO, A]
              ): Boolean =  {
    val actualResp         = actual.unsafeRunSync()
    val statusCheck        = actualResp.status == expectedStatus
    val bodyCheck          = expectedBody.fold[Boolean](
      actualResp.body.compile.toVector.unsafeRunSync().isEmpty)( // Verify Response's body is empty.
      expected => actualResp.as[A].unsafeRunSync() == expected
    )
    statusCheck && bodyCheck
  }

//  var httpApp: HttpRoutes[IO] = Router()

//  @Before
  def httpApp(): HttpRoutes[IO] =
    val services = SimpleHttpRoutes.helloWorldRoutes[IO]
               <+> SimpleHttpRoutes.tweetRoutes[IO]
    Router("/" ->  services)

  @Test def helloWorld(): Unit =
    val request = httpApp().run(Request(method = Method.GET, uri = uri"/hello/other-person" ))
    assertTrue(check(request.getOrElse(Response(NotFound)), Status.Ok, Some("{\"message\":\"Hello, other-person\"}")))

  @Test def specificTweet(): Unit =
    val request = httpApp().run(Request(method = Method.GET, uri = uri"/tweets/1234" ))
    assertTrue(check(request.getOrElse(Response(NotFound)), Status.Ok, Some("{\"tweet\":\"1234\"}")))

  @Test def popularTweets(): Unit =
    val expectedTweets = "[{\"tweet\":\"10\"},{\"tweet\":\"11\"},{\"tweet\":\"12\"},{\"tweet\":\"13\"},{\"tweet\":\"14\"},{\"tweet\":\"15\"},{\"tweet\":\"16\"},{\"tweet\":\"17\"}]"
    val request = httpApp().run(Request(method = Method.GET, uri = uri"/tweets/popular" ))
    assertTrue(check(request.getOrElse(Response(NotFound)), Status.Ok, Some(expectedTweets)))
