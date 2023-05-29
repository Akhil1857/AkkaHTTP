import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.ActorMaterializer

object Server extends App {
  val host = "localhost"
  val port = 9000

  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  private val route: Route = path("hello") { // we try to match the incoming request’s path as “/hello”. If it doesn’t match it will be rejected.
    Directives.get { //If it matches it will try to match inner “directives”. In our case we are matching GET requests.
      complete("Hello, World!") // We complete the request/response cycle with a “Hello, World” message.
    }
  }

  Http().bindAndHandle(route, host, port) // We are binding our route to the given host and port using the Akka HTTP Http object.
  println(s"Server is running at http://$host:$port/hello")

}