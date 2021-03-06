package controllers

import play.api._
import play.api.mvc._
import play.api.libs.ws._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.concurrent._
import scala.concurrent._

object Application extends Controller {
  
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def list = Action { request => 
    val futureList = models.Comics.listjson(request.remoteAddress)
    Async {
        futureList.map(json => Ok(json))
    }
  }

  def comic(id: String) = Action { request =>
    id.length match {
      case 0 => BadRequest("No id specified.")
      case _ => {
        Logger.info("New request for comic: " + id)
        try {
          val futureJson = models.Comics.comicJson(id)
          Async {      
            futureJson.map(json => Ok(json))
          }
        } catch {
          case _ => {
            Logger.error("No such comic: " + request)
            BadRequest("Error: No such comic: " + id)
          }
        }
      }
    }
  }
}
