package services

import modals.Transfer
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.db.Database
import play.api.test.Injecting

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Created by arpit on 23/08/17.
  */
class UserServiceSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "UserServiceSpec" should {
    "be able to get all users" in {
      val db = inject[Database]
      val service = new UserService(db)
      val result = service.getAllUser
      result.length mustBe(2)
    }

    "be able to transfer fund" in {
      val db = inject[Database]
      val service = new UserService(db)
      val transfer = Transfer("1", "2", 10)
      val result = Await.result(service.transfer(transfer), Duration.Inf)
      result mustBe("Transfer Complete")
    }

    "be able to show user not exist" in {
      val db = inject[Database]
      val service = new UserService(db)
      val transfer = Transfer("1", "20", 10)
      val result = Await.result(service.transfer(transfer), Duration.Inf)
      result mustBe("User not found")
    }

    "be able to show Not sufficient fund to transfer" in {
      val db = inject[Database]
      val service = new UserService(db)
      val transfer = Transfer("1", "2", 1000)
      val result = Await.result(service.transfer(transfer), Duration.Inf)
      result mustBe("Not sufficient fund to transfer")
    }
  }
}
