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

  /**
    * This function will fetch all the users from the H2 database
    *
    * @return
    */
  def getAllUser: mutable.MutableList[User] = {
    var users = mutable.MutableList.empty[User]
    db.withTransaction { conn =>
      val stmt = conn.createStatement
      val rs = stmt.executeQuery("select * from user")
      while (rs.next()) {
        val id = rs.getLong("id")
        val name = rs.getString("name")
        val balance = rs.getDouble("balance")
        users += User(id, name, balance)
      }
    }
    users
  }

  /**
    * This function will take the object of Transfer, we have to transfer money from one user account to another so we have to chekc few things first
    * 1. If those users exists
    * 2. If sender user have sufficient funds to transfer
    * 3. Now we are good to transfer funds from "from" to "to".
    *
    * @param transfer
    * @return
    */
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
          println(s"SQL exception = ${se}")
          conn.rollback
          throw se
      }
    }
}
