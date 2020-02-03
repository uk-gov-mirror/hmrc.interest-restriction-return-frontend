package views

import views.behaviours.ViewBehaviours
import views.html.$className$View
import views.{Twirl}

class $className$ViewSpec extends ViewBehaviours {

  lazy val twirlViewTemplate = viewFor[$className$View](Some(emptyUserAnswers))
  lazy val twirlView = twirlViewTemplate.apply()(fakeRequest, frontendAppConfig, messages)

      s"$className$View" must {

        behave like normalPage(twirlView, "$className;format="
        decap"$"
        )
        behave like pageWithBackLink(twirlView)
      }
  }
}
