package views

import views.behaviours.ViewBehaviours
import views.html.$className$View
import views.{Twirl}

class $className$ViewSpec extends ViewBehaviours {

  lazy val twirlViewTemplate = viewFor[$className$View](Some(emptyUserAnswers))
  lazy val twirlView = twirlViewTemplate.apply()(fakeRequest, frontendAppConfig, messages)
  lazy val View = await(Renderer.render($className$Template)(fakeRequest))

  Seq(twirlView -> Twirl).foreach {
    case (html, templatingSystem) =>
      s"$className$View (\$templatingSystem)" must {

        behave like normalPage(html, "$className;format="
        decap"$"
        )
        behave like pageWithBackLink(html)
      }
  }
}
