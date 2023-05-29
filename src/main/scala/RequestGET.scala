import RequestGET.system.dispatcher
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpMethods, HttpRequest}
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer

import java.net.URLEncoder
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps


object RequestGET extends App {

  private val host = "localhost"
  private val port = 8081
  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()


  val source =
    """
      |println("Hello World")
      |""".stripMargin

  val request = HttpRequest(
    method = HttpMethods.POST,
    uri = "http://localhost:8081",
    entity = HttpEntity(
      ContentTypes.`application/x-www-form-urlencoded`,
      s"source=${URLEncoder.encode(source.trim, "UTF-8")}&language=Scala&theme=Sunburst"
    )
  )

  private val bindingFuture = Http().singleRequest(request)
  bindingFuture.flatMap(_.entity.toStrict(2 seconds)).map(_.data.utf8String).foreach(println)
  println(s"Server is running on http://$host:$port")

}


