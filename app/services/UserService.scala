package services

import javax.inject.Inject

import modals.{Transfer, User}
import play.api.db.Database
import java.sql.SQLException

import scala.collection.mutable
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._

/**
  * Created by arpit on 21/08/17.
  */
class UserService @Inject()(db: Database) {

  def getAllUser: mutable.MutableList[User] = {

    var data = mutable.MutableList.empty[User]
    db.withTransaction { conn =>
      val stmt = conn.createStatement
      val rs = stmt.executeQuery("select * from user")
      while (rs.next()) {
        val id = rs.getLong("id")
        val name = rs.getString("name")
        val balance = rs.getDouble("balance")
        data += User(id, name, balance)
      }
    }
    data
  }


  def transfer(transfer: Transfer) = Future {

    val conn = db.getConnection()
      try {
        conn.setAutoCommit(false)
        val stmt = conn.createStatement
        val stmt1 = conn.createStatement

        val to = stmt1.executeQuery(s"select balance from user where id = ${transfer.to}")
        val from = stmt.executeQuery(s"select balance from user where id = ${transfer.from}")

       if(!from.next() || !to.next()) {
          println("User not found")
          "User not found"
        } else if(from.getDouble("balance") >= transfer.amount) {
          stmt.executeUpdate(s"UPDATE user SET balance = balance - ${transfer.amount} WHERE id = ${transfer.from};")
          stmt.executeUpdate(s"UPDATE user SET balance = balance + ${transfer.amount} WHERE id = ${transfer.to};")
          println("Transfer Complete")
         conn.commit
          "Transfer Complete"
        } else {
          println("Not sufficient fund to transfer")
          "Not sufficient fund to transfer"
        }

      } catch {
        case se: SQLException =>
          println(s"SQL exception >>> ${se}")
          conn.rollback
          throw se
      }
    }
}
