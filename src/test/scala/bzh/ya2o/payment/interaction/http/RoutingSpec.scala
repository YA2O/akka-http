package bzh.ya2o.payment.interaction.http

import org.scalatest.FunSuite
import akka.http.model.StatusCodes._
import akka.http.marshallers.sprayjson.SprayJsonSupport._
import akka.event.NoLogging
import akka.http.marshallers.sprayjson.SprayJsonSupport._
import akka.http.model.ContentTypes._
import akka.http.model.{HttpResponse, HttpRequest}
import akka.http.model.StatusCodes._
import akka.http.testkit.ScalatestRouteTest
import akka.stream.scaladsl.Flow
import org.scalatest._

//import akka.http.marshallers.sprayjson.SprayJsonSupport._

import akka.http.testkit.ScalatestRouteTest
import org.scalatest._

class RoutingSpec extends FunSuite with Matchers with ScalatestRouteTest {
  //  override def testConfigSource = "akka.loglevel = WARNING"
  //  def config = testConfig

  def routes = new Routing(executor).routes

  test("Service should respond with name from request") {
    Get("/hello/XXX") ~> routes ~> check {
      status shouldBe OK
//      contentType shouldBe `application/json`
//      responseAs[String].length should be > 0
      responseAs[String] shouldBe "Hello XXX!"

    }
  }

}
