package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.Json._

import scala.concurrent._
import scala.concurrent.duration._
import play.api.libs.concurrent.Akka

object Application extends Controller {
  
  def search(query: String) = Action {
    import Play.current
    import ExecutionContext.Implicits.global
    if (Play.current.configuration.getBoolean("down").getOrElse(false)) {
      val promise = Promise[Result]
      Akka.system.scheduler.scheduleOnce(30 seconds) {
        promise.success(InternalServerError)
      }
      Async {
        promise.future
      }
    } else {
      val promise = Promise[Result]
      Akka.system.scheduler.scheduleOnce(20 milliseconds) {
        promise.success(Ok(toJson(Map("query" -> toJson(query), "results" -> toJson(Seq(
          toJson(Map("productId" -> toJson(10), "description" -> toJson("Some product"))),
          toJson(Map("productId" -> toJson(11), "description" -> toJson("Some other product")))
        ))))))
      }
      Async {
        promise.future
      }
    }
  }
  
}
