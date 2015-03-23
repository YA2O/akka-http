package bzh.ya2o.payment.external

import akka.actor.{ActorRefFactory, ActorSystem}
import akka.stream.{Optimizations, ActorFlowMaterializerSettings, ActorFlowMaterializer}
import bzh.ya2o.payment.interaction.http.{Routing, AkkaConfiguration, HttpService}
import com.typesafe.config.{Config, ConfigFactory}

object Main extends App {
  implicit val config: Config = ConfigFactory.load()
  implicit val system: ActorSystem = ActorSystem()
  private val materializer = ActorFlowMaterializer()
  val akkaConfig = AkkaConfiguration(system, system.dispatcher, materializer)
  val routing = new Routing(system.dispatcher)
  val httpService = new HttpService(routing, akkaConfig)

  httpService.run()
}
