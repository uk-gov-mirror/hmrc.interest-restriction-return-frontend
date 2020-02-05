package views

import forms.$className;format="cap"$FormProvider
import models.{$className;format="cap"$, NormalMode}
import play.api.Application
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.CheckboxViewBehaviours
import views.html.$className;format="cap"$View
import controllers.$section$.routes.$className;format="cap"$Controller


class $className;format="cap"$ViewSpec extends CheckboxViewBehaviours[$className;format="cap"$] {

  val messageKeyPrefix = "$className;format="decap"$"
  val section = Some(messages(s"section.$section$"))
  val form = new $className;format="cap"$FormProvider()()

    s"$className;format="cap"$View" must {

      val view = viewFor[$className;format="cap"$View](Some(emptyUserAnswers))

      def applyView(form: Form[Set[$className;format="cap"$]]): HtmlFormat.Appendable = {
        val view = viewFor[$className;format="cap"$View](Some(emptyUserAnswers))
        view.apply(form, NormalMode)(fakeRequest, messages, frontendAppConfig)
      }

      behave like normalPage(applyView(form), messageKeyPrefix, section = section)

      behave like pageWithBackLink(applyView(form))

      behave like pageWithSubHeading(applyView(form), SectionHeaderMessages.$section$)

      behave like checkboxPage(form, applyView, messageKeyPrefix, $className;format="cap"$.options(form))

      behave like pageWithSubmitButton(applyView(form), BaseMessages.saveAndContinue)

      behave like pageWithSaveForLater(applyView(form))
  }
}
