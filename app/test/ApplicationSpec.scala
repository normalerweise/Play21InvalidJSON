import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json._
import play.api.http.HeaderNames
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import akka.util.Timeout
import play.api.libs.iteratee.Input
import play.api.http.Writeable
import play.api.libs.concurrent.Execution.Implicits._

@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends FunSpec with ShouldMatchers {
  
  implicit val localTimeout: Timeout = Timeout(500000)
  
  describe("Application") {
    
    it("should process json that is routed") {
      running(FakeApplication()) {
        
       val fr =  FakeRequest(GET, "/json").withJsonBody(Json.obj(
        "field1" -> "val1",
        "field2" -> "val2"))
        
        val result = route(fr).get
        
        status(result)(localTimeout) should equal (OK)
      }
    }
    
    it("should process json sent directly to the controller") {
      running(FakeApplication()) {
        
       val fr =  FakeRequest(GET, "/json").withJsonBody(Json.obj(
        "field1" -> "val1",
        "field2" -> "val2")).withHeaders(HeaderNames.CONTENT_TYPE -> "application/json")
        
        val result = processRequest(fr)
        
        println(contentAsString(result)(localTimeout))
        status(result)(localTimeout) should equal (OK)
      }
    }
    
    def processRequest[T](fr: FakeRequest[T])(implicit w: Writeable[T]) = {
      (controllers.Application.json(fr)).feed(Input.El(w.transform(fr.body))).flatMap(_.run)
    }
    
    it("should process json sent as a string directly to the controller") {
      running(FakeApplication()) {
        
       val fr =  FakeRequest(GET, "/json").withBody(Json.obj(
        "field1" -> "val1",
        "field2" -> "val2").toString).withHeaders(HeaderNames.CONTENT_TYPE -> "application/json")
        
        val result = processRequest(fr)
        
        println(contentAsString(result)(localTimeout))
        status(result)(localTimeout) should equal (OK)
      }
    }
    
  }
}
