import cats.effect.{IO, Sync}
import cats.implicits.*
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.dsl.io.{GET, Root}

object SimpleHttpRoutes {
  def helloWorldRoutes[F[_]: Sync](H: HelloWorld[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl.*
    HttpRoutes.of[F] {
      case GET -> Root / "hello" / name =>
        for {
          greeting <- H.hello(HelloWorld.Name(name))
          resp <- Ok(greeting)
        } yield resp
    }
  }

  def tweetRoutes[F[_]: Sync](T: Tweet[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl.*
    HttpRoutes.of[F] {
      case GET -> Root / "tweets" / "popular" =>
        for {
          resp <- T.getPopularTweets.flatMap(Ok(_))
        } yield resp
      case GET -> Root / "tweets" / IntVar(tweetId) =>
        for {
          resp <- T.getTweet(Tweet.Name(tweetId)).flatMap(Ok(_))
        } yield resp
    }
  }
}