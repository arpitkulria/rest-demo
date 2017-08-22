import javax.inject.Inject

import services.SetupDB

class Global @Inject()(setupDB: SetupDB) {

  println("In global module, setting up initial database")
  setupDB.setUpDB
}
