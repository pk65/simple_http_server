import cats.effect.{IO, LiftIO}
import org.junit.Test
import org.junit.Assert.*


class Test1:

  @Test def t1(): Unit =
    val msg = "I was compiled by Scala 3. :)"
    assertEquals("I was compiled by Scala 3. :)", msg)
