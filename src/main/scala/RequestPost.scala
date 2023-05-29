import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpMethods, HttpRequest}
import akka.stream.ActorMaterializer

import java.net.URLEncoder
import scala.concurrent.duration.DurationInt

object RequestPost extends App{

  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  import system.dispatcher

  val source =
    """
      |object SimpleApp {
      |  val aField = 2
      |
      |  def aMethod(x: Int) = x + 1
      |
      |  def main(args: Array[String]) = {
      |    println(aMethod(aField))
      |  }
      |}
  """.stripMargin

  val request = HttpRequest(
    method = HttpMethods.POST,
    uri = "http://localhost:8081",
    entity = HttpEntity(
      ContentTypes.`application/x-www-form-urlencoded`,
      s"source=${URLEncoder.encode(source.trim, "UTF-8")}&language=Scala&theme=Sunburst"
    )
  )

  def simpleRequest() = {
    val responseFuture = Http().singleRequest(request)
    responseFuture.flatMap(_.entity.toStrict(2.seconds)).map(_.data.utf8String).foreach(println)
  }

  println(simpleRequest())
}
