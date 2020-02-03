package views

import views.behaviours.ViewBehaviours
import views.html.$className;format="cap"$View
import views.{Twirl}

class $className;format="cap"$ViewSpec extends ViewBehaviours {

  lazy val twirlViewTemplate = viewFor[$className;format="cap"$View](Some(emptyUserAnswers))
  lazy val twirlView = twirlViewTemplate.apply()(fakeRequest, frontendAppConfig, messages)

      s"$className;format="cap"$View" must {

        behave like normalPage(twirlView, "$className;format="decap"$"
        )
        behave like pageWithBackLink(twirlView)
      }
  }
}
