package views

import views.behaviours.ViewBehaviours
import views.html.$section;format="decap"$.$className;format="cap"$View
import views.{Twirl}

class $className;format="cap"$ViewSpec extends ViewBehaviours {

  lazy val twirlViewTemplate = viewFor[$className;format="cap"$View](Some(emptyUserAnswers))
  lazy val twirlView = twirlViewTemplate.apply()(fakeRequest, frontendAppConfig, messages)

  val messageKeyPrefix = "$className;format="decap"$"
  val section = Some(messages("section.$section;format="decap"$"))
  val form = new $className;format="cap"$FormProvider()()

      "$className;format="cap"$View" must {

        behave like normalPage(twirlView, messageKeyPrefix, section = section)
        behave like pageWithBackLink(twirlView)
      }
  }
