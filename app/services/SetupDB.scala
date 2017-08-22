package services

import javax.inject.Inject

import play.api.db.Database

/**
  * Created by arpit on 21/08/17.
  */
class SetupDB @Inject()(db: Database) {

  def setUpDB = {
    db.withTransaction { conn =>
      val stmt = conn.createStatement
      stmt.executeUpdate("create table if not exists user (id long, name varchar, balance double precision);")
      stmt.executeUpdate("delete from user;")
      stmt.executeUpdate("insert into user values(1, 'user1', 100);")
      stmt.executeUpdate("insert into user values(2, 'user2', 100);")
    }
  }
}
