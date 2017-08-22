import com.google.inject.AbstractModule


class GlobalModule extends AbstractModule {
  protected def configure(): Unit = {
    bind(classOf[Global]).asEagerSingleton
  }
}
