import cats.Applicative
import cats.implicits.*
import io.circe.{Encoder, Json}
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

trait Tweet[F[_]]{
  def getTweet(tweetId: Tweet.Name): F[Tweet.Greeting]
  def getPopularTweets: F[Seq[Tweet.Greeting]]
}

object Tweet {
  implicit def apply[F[_]](implicit ev: Tweet[F]): Tweet[F] = ev

  final case class Name(tweetId: Int) extends AnyVal

  final case class Greeting(greeting: String) extends AnyVal
  object Greeting {
    implicit val greetingEncoder: Encoder[Greeting] = (a: Greeting) => Json.obj(
      ("tweet", Json.fromString(a.greeting)),
    )
    implicit def greetingEntityEncoder[F[_]]: EntityEncoder[F, Greeting] =
      jsonEncoderOf[F, Greeting]
    implicit def greetingsEntityEncoder[F[_]]: EntityEncoder[F, Seq[Greeting]] =
      jsonEncoderOf[F, Seq[Greeting]]
  }

  def impl[F[_]: Applicative]: Tweet[F] = new Tweet[F]{
    def getTweet(n: Tweet.Name): F[Tweet.Greeting] =
      Greeting(n.tweetId.toString).pure[F]
    def getPopularTweets: F[Seq[Tweet.Greeting]] =
      (10 to 17).map(e => Greeting(e.toString)).pure[F]
  }
}
