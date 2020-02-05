package views

import controllers.routes
import forms.$className;format="cap"$FormProvider
import models.NormalMode
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.DecimalViewBehaviours
import views.html.$className;format="cap"$View

class $className;format="cap"$ViewSpec extends DecimalViewBehaviours  {

  val messageKeyPrefix = "$className;format="decap"$"

  val form = new $className;format="cap"$FormProvider()()

    "$className;format="cap"$View" must {

      def applyView(form: Form[_]): HtmlFormat.Appendable = {
          val view = viewFor[$className;format="cap"$View](Some(emptyUserAnswers))
          view.apply(form, NormalMode)(fakeRequest, messages, frontendAppConfig)
        }

      behave like normalPage(applyView(form), messageKeyPrefix, section = section)

      behave like pageWithBackLink(applyView(form))

      behave like pageWithSubHeading(applyView(form), SectionHeaderMessages.$section$)

      behave like decimalPage(form, applyView, messageKeyPrefix, routes.$className;format="cap"$Controller.onSubmit(NormalMode).url, section = section)

      behave like pageWithSubmitButton(applyView(form), BaseMessages.saveAndContinue)

      behave like pageWithSaveForLater(applyView(form))
    }
  }

