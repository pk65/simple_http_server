import cats.effect.IO
import munit.CatsEffectSuite
import org.http4s.*
import org.http4s.Status.NotFound
import org.http4s.implicits.*
import org.http4s.server.Router

class TweetSpec extends CatsEffectSuite {
  private def get(uri: Uri) =
    val request = Router("/" -> SimpleHttpRoutes.tweetRoutes[IO])
      .run(Request(method = Method.GET, uri = uri))
    request.getOrElse(Response[IO](NotFound))

  test("Popular tweets return series of tweets") {
    val expectedTweets = "[{\"tweet\":\"10\"},{\"tweet\":\"11\"},{\"tweet\":\"12\"},{\"tweet\":\"13\"}," +
      "{\"tweet\":\"14\"},{\"tweet\":\"15\"},{\"tweet\":\"16\"},{\"tweet\":\"17\"}]"
    val retPopularTweets = get(uri"/tweets/popular")
    for {
      _ <- assertIO(retPopularTweets.flatMap(_.as[String]), expectedTweets)
      _ <- assertIO(retPopularTweets.map(_.status), Status.Ok)
    } yield ()
  }

  test("Tweet can be found by ID") {
    val retSpecificTweet = get(uri"/tweets/1234")
    for {
      _ <- assertIO(retSpecificTweet.flatMap(_.as[String]), "{\"tweet\":\"1234\"}")
      _ <- assertIO(retSpecificTweet.map(_.status), Status.Ok)
    } yield ()
  }
}
