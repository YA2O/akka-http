package bzh.ya2o.payment.interaction.http

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.Http
import akka.http.server.Route
import akka.http.server.directives.MethodDirectives._
import akka.http.server.directives.PathDirectives._
import akka.http.server.directives.RouteDirectives._
import akka.http.testkit.RouteTest
import akka.stream.ActorFlowMaterializer
import akka.stream.scaladsl.Sink
import com.typesafe.config.Config

import scala.concurrent.{ExecutionContextExecutor, Future}

final case class AkkaConfiguration(system: ActorSystem, executor: ExecutionContextExecutor, materializer: ActorFlowMaterializer)


final class HttpService(routing: Routing, akkaConfig: AkkaConfiguration)(implicit val config: Config) {

   implicit val system: ActorSystem = akkaConfig.system
   implicit val executor: ExecutionContextExecutor = akkaConfig.executor
   implicit val materializer: ActorFlowMaterializer = akkaConfig.materializer
   val logger: LoggingAdapter = Logging(system, getClass)

  val handlerAsync = Route.asyncHandler(routing.routes)
  //  val handlerSync: HttpRequest => HttpResponse = {
  //    case HttpRequest(GET, Uri.Path("/"), _, _, _) => HttpResponse(
  //      entity = HttpEntity(
  //        MediaTypes.`text/html`,
  //        "<html><body>Hello world!</body></html>"
  //      )
  //    )
  //    case HttpRequest(GET, Uri.Path("/ping"), _, _, _) => HttpResponse(entity = "PONG!")
  //    case HttpRequest(GET, Uri.Path("/crash"), _, _, _) => sys.error("BOOM!")
  //    case _: HttpRequest => HttpResponse(StatusCodes.NotFound, entity = "Unknown resource!")
  //  }

  def run(): Future[Unit] = {
    Http().bind(
      interface = config.getString("http.interface"),
      port = config.getInt("http.port")
    )
      .runWith(
        Sink.foreach { connection ⇒
          connection.handleWithAsyncHandler(handlerAsync)
          //          connection handleWithSyncHandler handlerSync
        }
      )
  }
}

class Routing(val executor: ExecutionContextExecutor) {
  implicit val executorImplicit: ExecutionContextExecutor = executor

  val routes: Route = path("hello" / Segment) { name ⇒
    get {
      complete { s"Hello $name!" }
    }
  }
}
