package test

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class ApplicationSpec extends Specification {
  
  "Application" should {
    
    "valid json" in {
      running(FakeApplication()) {
        
       val fr =  FakeRequest(GET, "/json").withJsonBody(Json.obj(
        "field1" -> "val1",
        "field2" -> "val2"))
        
        val result = route(fr).get
        
        status(result) must equalTo(OK)
      }
    }
    
     "invalid json" in {
      running(FakeApplication()) {
        
       val fr =  FakeRequest(GET, "/json").withJsonBody(Json.obj(
        "field1" -> "val1",
        "field2" -> "val2"))
        
        val result = await(controllers.Application.json(fr).run)
        
        println(contentAsString(result));
        status(result) must equalTo(OK)
      }
    }
  }
}