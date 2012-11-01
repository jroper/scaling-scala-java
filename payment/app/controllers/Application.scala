package controllers

import play.api._
import play.api.mvc._
import play.api.Play.current

import scala.concurrent._
import scala.concurrent.duration._
import play.api.libs.concurrent.Akka

object Application extends Controller {
  
  def payments = Action(parse.json) { request =>
    import Play.current
    import ExecutionContext.Implicits.global
      val promise = Promise[Result]
      Akka.system.scheduler.scheduleOnce(20 milliseconds) {
        promise.success(Ok("Receieved payment of " + (request.body \ "amount")))
      }
      Async {
        promise.future
      }
  }
  
}
