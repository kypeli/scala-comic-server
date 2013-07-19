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

  def list = Action {
    Ok(models.Comics.listjson)

/*    val httpResponse = WS.url("http://www.johanpaul.com/blog/")
    Async {
      httpResponse.get.map { request =>
        Ok(request.body)
      }
    }
*/    
  }

  def comic(id: String) = Action { request =>
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
/*      id.length match {
          case 0 => BadRequest("No id specified")
          case _ => {
            val futureResponse: Future[play.api.libs.ws.Response] = models.Comics.comicJson(id)
            futureResponse onSuccess {
               case response => Ok(response.body)
            }
          }
      }*/
  }
}
