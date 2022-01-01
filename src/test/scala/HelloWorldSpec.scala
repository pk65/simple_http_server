import cats.effect.IO
import cats.syntax.all.*
import munit.CatsEffectSuite
import org.http4s.*
import org.http4s.Status.NotFound
import org.http4s.implicits.*
import org.http4s.server.Router

class HelloWorldSpec extends CatsEffectSuite {
  private def httpResp(uri: Uri) =
    val services = SimpleHttpRoutes.helloWorldRoutes[IO]
      <+> SimpleHttpRoutes.tweetRoutes[IO]
    val request = Router("/" -> services).run(Request(method = Method.GET, uri = uri))
    request.getOrElse(Response[IO](NotFound))

  private val retSpecificTweet: IO[Response[IO]] = httpResp(uri"/tweets/1234")
  private val retPopularTweets: IO[Response[IO]] = httpResp(uri"/tweets/popular")
  private val retHelloWorld: IO[Response[IO]] = httpResp(uri"/hello/other-person")

  test("HelloWorld returns status code 200") {
    assertIO(retHelloWorld.map(_.status), Status.Ok)
  }

  test("HelloWorld returns hello world message") {
    assertIO(retHelloWorld.flatMap(_.as[String]), "{\"message\":\"Hello, other-person\"}")
  }

  test("Tweets popular returns status code 200") {
    assertIO(retPopularTweets.map(_.status), Status.Ok)
  }

  test("Tweets popular returns series of tweets") {
    val expectedTweets = "[{\"tweet\":\"10\"},{\"tweet\":\"11\"},{\"tweet\":\"12\"},{\"tweet\":\"13\"}," +
      "{\"tweet\":\"14\"},{\"tweet\":\"15\"},{\"tweet\":\"16\"},{\"tweet\":\"17\"}]"
    assertIO(retPopularTweets.flatMap(_.as[String]), expectedTweets)
  }

  test("Search for specific tweet returns status code 200") {
    assertIO(retSpecificTweet.map(_.status), Status.Ok)
  }

  test("Tweet can be found by ID") {
    assertIO(retSpecificTweet.flatMap(_.as[String]), "{\"tweet\":\"1234\"}")
  }
}
