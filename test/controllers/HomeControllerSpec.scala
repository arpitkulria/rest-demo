package controllers

import modals.Transfer
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test.Helpers._
import play.api.test._
import services.UserService

import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration
import org.mockito.Mockito._
import play.api.libs.concurrent.Execution.Implicits._


/**
  * Add your spec here.
  * You can mock out a whole application including requests, plugins etc.
  *
  * For more information, see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
  */
class HomeControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting with MockitoSugar {

  "HomeController GET" should {

    "render the index page from the application" in {
      val controller = inject[HomeController]
      val home = controller.index().apply(FakeRequest(GET, "/"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include("Balance")
    }

    "be able to transfer funds" in {
      val userService = inject[UserService]
      val controller = inject[HomeController]
      val home = controller.transfer()(FakeRequest(POST, "/transfer").withFormUrlEncodedBody("from" -> "1", "to" -> "2", "amount" -> "10"))

      status(home) mustBe SEE_OTHER
      redirectLocation(home).get must include("/")
      implicit val time = akka.util.Timeout(5, java.util.concurrent.TimeUnit.SECONDS)
      (flash(home)(time)) ("SUCCESS") must include("Transfer")
    }

    "be able to show error" in {
      val controller = inject[HomeController]
      val mockUserService = mock[UserService]
      val trans = Transfer("1", "1", 1)
      when(mockUserService.transfer(trans)) thenReturn Future.failed(new Throwable("ERROR"))
      val home = controller.transfer()(FakeRequest(POST, "/transfer"))
      status(home) mustBe SEE_OTHER
      redirectLocation(home).get must include("/")
      implicit val time = akka.util.Timeout(5, java.util.concurrent.TimeUnit.SECONDS)
      (flash(home)(time)) ("ERROR") must include("There was some problem with from data")
    }
  }
}
