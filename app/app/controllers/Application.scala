package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._

object Application extends Controller {

  def json = Action(parse.json) {
    request =>
      // .. actually we use the json obj in the body
      Ok(Json.obj(("result" -> "success")))
  }

}