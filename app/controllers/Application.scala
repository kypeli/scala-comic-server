package controllers

import play.api._
import play.api.mvc._
import play.api.libs.ws.WS
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.concurrent._

object Application extends Controller {
  
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def list = Action {
    Ok(models.ComicList.json)

/*    val httpResponse = WS.url("http://www.johanpaul.com/blog/")
    Async {
      httpResponse.get.map { request =>
        Ok(request.body)
      }
    }
*/    
  }
}
