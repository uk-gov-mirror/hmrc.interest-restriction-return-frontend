package views

import forms.$className;format="cap"$FormProvider
import models.{$className;format="cap"$, NormalMode}
import play.api.Application
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.CheckboxViewBehaviours
import views.html.$className;format="cap"$View

class $className;format="cap"$ViewSpec extends CheckboxViewBehaviours[$className;format="cap"$] {

  val messageKeyPrefix = "$className;format="decap"$"

  val form = new $className;format="cap"$FormProvider()()

    s"$className;format="cap"$View" must {

      val view = viewFor[$className;format="cap"$View](Some(emptyUserAnswers))

      def applyView(form: Form[Set[$className;format="cap"$]]): HtmlFormat.Appendable = {
        val view = viewFor[$className;format="cap"$View](Some(emptyUserAnswers))
        view.apply(form, NormalMode)(fakeRequest, messages, frontendAppConfig)
      }

      behave like normalPage(applyView(form), messageKeyPrefix)

      behave like pageWithBackLink(applyView(form))

      behave like checkboxPage(form, applyView, messageKeyPrefix, $className;format="cap"$.options(form))
    }
  }
}

